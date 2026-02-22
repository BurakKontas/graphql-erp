package tr.kontas.erp.app.tenant.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.tenant.dtos.*;
import tr.kontas.erp.core.application.tenant.*;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.tenant.LdapSettings;
import tr.kontas.erp.core.domain.tenant.OidcSettings;
import tr.kontas.erp.core.domain.tenant.Tenant;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class TenantGraphql {

    private final CreateTenantUseCase createTenantUseCase;
    private final GetTenantsUseCase getTenantsUseCase;
    private final GetTenantByIdUseCase getTenantByIdUseCase;
    private final UpdateTenantAuthModeUseCase updateTenantAuthModeUseCase;
    private final UpdateTenantOidcSettingsUseCase updateTenantOidcSettingsUseCase;
    private final UpdateTenantLdapSettingsUseCase updateTenantLdapSettingsUseCase;

    public static TenantPayload toPayload(Tenant domain) {
        return new TenantPayload(
                domain.getId().asUUID().toString(),
                domain.getName().getValue(),
                domain.getCode().getValue(),
                domain.getAuthMode().name()
        );
    }

    private Tenant findTenantById(String tenantId) {
        return getTenantByIdUseCase.execute(TenantId.of(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantId));
    }

    @DgsMutation
    public TenantPayload createTenant(@InputArgument("input") CreateTenantInput input) {

        CreateTenantCommand command = new CreateTenantCommand(input.getName(), input.getCode());

        TenantId tenantId = createTenantUseCase.execute(command);

        return new TenantPayload(
                tenantId.asUUID().toString(),
                input.getName(),
                input.getCode(),
                AuthProviderType.LOCAL.name()
        );
    }

    @DgsMutation
    public TenantPayload updateTenantAuthMode(@InputArgument("input") UpdateTenantAuthModeInput input) {
        AuthProviderType authMode;
        try {
            authMode = AuthProviderType.valueOf(input.getAuthMode().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid auth mode: " + input.getAuthMode() +
                    ". Valid values: LOCAL, LDAP, OIDC");
        }

        UpdateTenantAuthModeCommand command = new UpdateTenantAuthModeCommand(
                input.getTenantId(),
                authMode
        );

        updateTenantAuthModeUseCase.execute(command);

        return toPayload(findTenantById(input.getTenantId()));
    }

    @DgsMutation
    public TenantPayload updateTenantOidcSettings(@InputArgument("input") UpdateTenantOidcSettingsInput input) {
        OidcSettings oidcSettings = OidcSettings.builder()
                .issuer(input.getIssuer())
                .audience(input.getAudience())
                .jwkSetUri(input.getJwkSetUri())
                .clockSkewSeconds(input.getClockSkewSeconds() != null ? input.getClockSkewSeconds() : 60L)
                .build();

        UpdateTenantOidcSettingsCommand command = new UpdateTenantOidcSettingsCommand(
                input.getTenantId(),
                oidcSettings
        );

        updateTenantOidcSettingsUseCase.execute(command);

        return toPayload(findTenantById(input.getTenantId()));
    }

    @DgsMutation
    public TenantPayload updateTenantLdapSettings(@InputArgument("input") UpdateTenantLdapSettingsInput input) {
        LdapSettings ldapSettings = LdapSettings.builder()
                .urls(input.getUrls())
                .baseDn(input.getBaseDn())
                .userSearchFilter(input.getUserSearchFilter())
                .bindDn(input.getBindDn())
                .bindPassword(input.getBindPassword())
                .connectTimeoutMs(input.getConnectTimeoutMs() != null ? input.getConnectTimeoutMs() : 5000)
                .readTimeoutMs(input.getReadTimeoutMs() != null ? input.getReadTimeoutMs() : 5000)
                .startTls(input.getStartTls() != null && input.getStartTls())
                .maxRetry(input.getMaxRetry() != null ? input.getMaxRetry() : 2)
                .circuitBreakerTimeoutMs(input.getCircuitBreakerTimeoutMs() != null ? input.getCircuitBreakerTimeoutMs() : 30000L)
                .resolveGroups(input.getResolveGroups() != null && input.getResolveGroups())
                .groupSearchFilter(input.getGroupSearchFilter() != null ? input.getGroupSearchFilter() : "(member={0})")
                .build();

        UpdateTenantLdapSettingsCommand command = new UpdateTenantLdapSettingsCommand(
                input.getTenantId(),
                ldapSettings
        );

        updateTenantLdapSettingsUseCase.execute(command);

        return toPayload(findTenantById(input.getTenantId()));
    }

    @DgsQuery
    public List<TenantPayload> tenants() {
        return getTenantsUseCase.execute()
                .stream()
                .map(TenantGraphql::toPayload)
                .toList();
    }

    @DgsQuery
    public TenantPayload tenant(@InputArgument("id") String id) {
        return toPayload(findTenantById(id));
    }

    @DgsEntityFetcher(name = "TenantPayload")
    public TenantPayload tenant(Map<String, Object> values) {
        Object id = values.get("id");
        if (id == null) {
            throw new IllegalArgumentException("TenantPayload entity fetcher requires 'id'");
        }
        return tenant(id.toString());
    }

    @DgsData(parentType = "TenantPayload")
    public CompletableFuture<List<CompanyPayload>> companies(DgsDataFetchingEnvironment dfe) {
        TenantPayload employee = dfe.getSource();

        DataLoader<String, List<CompanyPayload>> dataLoader = dfe.getDataLoader("companiesByTenantDataLoader");

        if (dataLoader == null || employee == null) {
            return CompletableFuture.completedFuture(List.of());
        }

        return dataLoader.load(employee.getId());
    }
}
