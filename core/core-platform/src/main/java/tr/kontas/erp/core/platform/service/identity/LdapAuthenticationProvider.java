package tr.kontas.erp.core.platform.service.identity;

import org.springframework.stereotype.Component;
import tr.kontas.erp.core.application.identity.AuthenticationCommand;
import tr.kontas.erp.core.application.identity.AuthenticationProvider;
import tr.kontas.erp.core.application.identity.AuthenticationResult;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.identity.valueobjects.ExternalIdentity;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.domain.identity.valueobjects.UserName;
import tr.kontas.erp.core.domain.tenant.LdapSettings;
import tr.kontas.erp.core.domain.tenant.Tenant;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    private final TenantRepository tenantRepository;
    private final Map<String, Long> circuitBreaker = new ConcurrentHashMap<>();

    public LdapAuthenticationProvider(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public boolean supports(AuthProviderType provider) {
        return provider == AuthProviderType.LDAP;
    }

    @Override
    public AuthenticationResult authenticate(AuthenticationCommand command) {

        String tenantId = command.tenantId();
        String username = (String) command.attributes().get("username");
        String password = (String) command.attributes().get("password");

        if (tenantId == null || username == null || password == null) {
            throw new IllegalArgumentException("Missing LDAP credentials");
        }

        LdapSettings config = getLdapSettings(tenantId);

        checkCircuitBreaker(tenantId, config);

        String userDn = findUserDn(config, username);

        bindAsUser(config, userDn, password);

        return new AuthenticationResult(
                UserId.newId().asUUID(),
                new UserName(username),
                new ExternalIdentity(AuthProviderType.LDAP, userDn),
                Set.of()
        );
    }

    private LdapSettings getLdapSettings(String tenantId) {
        Tenant tenant = tenantRepository.findById(TenantId.of(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Unknown tenant: " + tenantId));

        LdapSettings settings = tenant.getLdapSettings();
        if (settings == null || !settings.isConfigured()) {
            throw new IllegalStateException("LDAP is not configured for tenant: " + tenantId);
        }

        return settings;
    }

    private String findUserDn(LdapSettings config, String username) {

        for (String url : config.getUrls()) {

            for (int retry = 0; retry <= config.getMaxRetry(); retry++) {

                try {
                    LdapContext ctx = createContext(
                            url,
                            config.getBindDn(),
                            config.getBindPassword(),
                            config);

                    String safeUsername = escapeLdap(username);
                    String filter = config.getUserSearchFilter()
                            .replace("{0}", safeUsername);

                    SearchControls controls = new SearchControls();
                    controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

                    NamingEnumeration<SearchResult> results =
                            ctx.search(config.getBaseDn(), filter, controls);

                    if (!results.hasMore()) {
                        throw new IllegalArgumentException("LDAP user not found");
                    }

                    SearchResult result = results.next();
                    return result.getNameInNamespace();

                } catch (Exception e) {
                    handleFailure(config, e);
                }
            }
        }

        throw new IllegalStateException("LDAP search failed after retries");
    }

    private void bindAsUser(LdapSettings config, String userDn, String password) {

        for (String url : config.getUrls()) {
            try {
                createContext(url, userDn, password, config);
                return;
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException("Invalid LDAP credentials");
    }

    private LdapContext createContext(String url,
                                      String principal,
                                      String credentials,
                                      LdapSettings config)
            throws Exception {

        Hashtable<String, String> env = new Hashtable<>();

        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");

        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, principal);
        env.put(Context.SECURITY_CREDENTIALS, credentials);

        env.put("com.sun.jndi.ldap.connect.timeout",
                String.valueOf(config.getConnectTimeoutMs()));

        env.put("com.sun.jndi.ldap.read.timeout",
                String.valueOf(config.getReadTimeoutMs()));

        env.put("com.sun.jndi.ldap.connect.pool", "true");

        LdapContext ctx = new InitialLdapContext(env, null);

        if (config.isStartTls()) {
            StartTlsResponse tls =
                    (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());
            tls.negotiate();
        }

        return ctx;
    }

    private void checkCircuitBreaker(String tenantId, LdapSettings config) {
        Long lastFailure = circuitBreaker.get(tenantId);

        if (lastFailure == null) return;

        if (System.currentTimeMillis() - lastFailure < config.getCircuitBreakerTimeoutMs()) {
            throw new IllegalStateException("LDAP temporarily unavailable (circuit open)");
        }
    }

    private void handleFailure(LdapSettings config, Exception e) {
        circuitBreaker.put(config.getBaseDn(), System.currentTimeMillis());
    }

    private String escapeLdap(String input) {
        return input
                .replace("\\", "\\5c")
                .replace("*", "\\2a")
                .replace("(", "\\28")
                .replace(")", "\\29")
                .replace("\0", "\\00");
    }
}
