package tr.kontas.erp.core.platform.service.identity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.tenant.OidcSettings;
import tr.kontas.erp.core.domain.tenant.Tenant;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwkKeyProvider {

    private static final long CIRCUIT_BREAKER_TIMEOUT_MS = 30_000;
    private final TenantRepository tenantRepository;
    private final ObjectMapper mapper;
    private final Cache<String, Map<String, RSAPublicKey>> tenantKeyCache =
            Caffeine.newBuilder()
                    .maximumSize(100)
                    .expireAfterWrite(Duration.ofMinutes(15))
                    .build();
    private final Map<String, Long> lastFailureTime = new ConcurrentHashMap<>();

    public JwkKeyProvider(TenantRepository tenantRepository, ObjectMapper mapper) {
        this.tenantRepository = tenantRepository;
        this.mapper = mapper;
    }

    public RSAPublicKey resolve(String tenantId, String kid) {
        Map<String, RSAPublicKey> keys = tenantKeyCache.get(tenantId,
                this::fetchKeysForTenant);

        RSAPublicKey key = keys.get(kid);

        if (key == null) {
            tenantKeyCache.invalidate(tenantId);
            keys = fetchKeysForTenant(tenantId);
            key = keys.get(kid);
        }

        if (key == null) {
            throw new IllegalStateException("No matching key for kid=" + kid);
        }

        return key;
    }

    private Map<String, RSAPublicKey> fetchKeysForTenant(String tenantId) {
        checkCircuitBreaker(tenantId);

        try {
            Tenant tenant = tenantRepository.findById(TenantId.of(tenantId))
                    .orElseThrow(() -> new IllegalArgumentException("Unknown tenant: " + tenantId));

            OidcSettings config = tenant.getOidcSettings();
            if (config == null || !config.isConfigured()) {
                throw new IllegalStateException("OIDC is not configured for tenant: " + tenantId);
            }

            URL url = URI.create(config.getJwkSetUri()).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() != 200) {
                throw new IllegalStateException("JWK fetch failed");
            }

            JsonNode root = mapper.readTree(connection.getInputStream());
            JsonNode keysNode = root.get("keys");

            Map<String, RSAPublicKey> result = new HashMap<>();

            for (JsonNode keyNode : keysNode) {

                if (!"RSA".equals(keyNode.get("kty").asText())) continue;

                String kid = keyNode.get("kid").asText();
                String n = keyNode.get("n").asText();
                String e = keyNode.get("e").asText();

                result.put(kid, buildKey(n, e));
            }

            return result;

        } catch (Exception ex) {
            lastFailureTime.put(tenantId, System.currentTimeMillis());
            throw new IllegalStateException("Failed to fetch JWK", ex);
        }
    }

    private void checkCircuitBreaker(String tenantId) {
        Long lastFailure = lastFailureTime.get(tenantId);
        if (lastFailure == null) return;

        if (System.currentTimeMillis() - lastFailure < CIRCUIT_BREAKER_TIMEOUT_MS) {
            throw new IllegalStateException("JWK endpoint temporarily disabled (circuit open)");
        }
    }

    private RSAPublicKey buildKey(String n, String e) throws Exception {
        byte[] modulusBytes = Base64.getUrlDecoder().decode(n);
        byte[] exponentBytes = Base64.getUrlDecoder().decode(e);

        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger exponent = new BigInteger(1, exponentBytes);

        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
