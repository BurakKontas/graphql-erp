package tr.kontas.erp.core.platform.service.identity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.application.identity.AuthenticationCommand;
import tr.kontas.erp.core.application.identity.AuthenticationProvider;
import tr.kontas.erp.core.application.identity.AuthenticationResult;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.identity.valueobjects.ExternalIdentity;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.domain.identity.valueobjects.UserName;
import tr.kontas.erp.core.domain.tenant.OidcSettings;
import tr.kontas.erp.core.domain.tenant.Tenant;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Set;

@Component
public class OidcAuthenticationProvider implements AuthenticationProvider {

    private final JwkKeyProvider keyProvider;
    private final TenantRepository tenantRepository;

    public OidcAuthenticationProvider(JwkKeyProvider keyProvider,
                                      TenantRepository tenantRepository) {
        this.keyProvider = keyProvider;
        this.tenantRepository = tenantRepository;
    }

    @Override
    public boolean supports(AuthProviderType provider) {
        return provider == AuthProviderType.OIDC;
    }

    @Override
    public AuthenticationResult authenticate(AuthenticationCommand command) {
        String idToken = (String) command.attributes().get("id_token");
        String tenantId = command.tenantId();

        if (idToken == null || idToken.isBlank()) {
            throw new IllegalArgumentException("Missing id_token");
        }

        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("Missing tenantId");
        }

        OidcSettings config = getOidcSettings(tenantId);
        Claims claims = validate(idToken, tenantId, config);

        return new AuthenticationResult(
                UserId.newId().asUUID(),
                new UserName(extractUsername(claims)),
                new ExternalIdentity(AuthProviderType.OIDC, claims.getSubject()),
                Set.of()
        );
    }

    private OidcSettings getOidcSettings(String tenantId) {
        Tenant tenant = tenantRepository.findById(TenantId.of(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Unknown tenant: " + tenantId));

        OidcSettings settings = tenant.getOidcSettings();
        if (settings == null || !settings.isConfigured()) {
            throw new IllegalStateException("OIDC is not configured for tenant: " + tenantId);
        }

        return settings;
    }

    private Claims validate(String token, String tenantId, OidcSettings config) {
        try {
            var unsigned = Jwts.parser()
                    .build()
                    .parseSignedClaims(token);

            String kid = unsigned.getHeader().getKeyId();
            String alg = (String) unsigned.getHeader().get("alg");

            if (kid == null || kid.isBlank()) {
                throw new IllegalArgumentException("Missing kid in JWT header");
            }

            if (!"RS256".equals(alg)) {
                throw new IllegalArgumentException("Unsupported JWT algorithm: " + alg);
            }

            RSAPublicKey key = keyProvider.resolve(tenantId, kid);

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .clockSkewSeconds(config.getClockSkewSeconds())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            validateClaims(claims, config);

            return claims;

        } catch (JwtException ex) {
            throw new IllegalArgumentException("Invalid OIDC token", ex);
        }
    }

    private void validateClaims(Claims claims, OidcSettings config) {
        // issuer
        if (!config.getIssuer().equals(claims.getIssuer())) {
            throw new IllegalArgumentException("Invalid issuer");
        }

        // audience
        if (config.getAudience() != null && !config.getAudience().isBlank()) {
            if (claims.getAudience() == null ||
                    !claims.getAudience().contains(config.getAudience())) {
                throw new IllegalArgumentException("Invalid audience");
            }
        }

        Date now = new Date();

        // exp
        if (claims.getExpiration() == null ||
                claims.getExpiration().before(now)) {
            throw new IllegalArgumentException("Token expired");
        }

        // nbf
        if (claims.getNotBefore() != null &&
                claims.getNotBefore().after(now)) {
            throw new IllegalArgumentException("Token not yet valid");
        }

        // issued-at sanity check
        if (claims.getIssuedAt() != null &&
                claims.getIssuedAt().after(new Date(now.getTime() + 300_000))) {
            throw new IllegalArgumentException("Invalid issued-at claim");
        }

        // subject required
        if (claims.getSubject() == null || claims.getSubject().isBlank()) {
            throw new IllegalArgumentException("Missing subject claim");
        }
    }

    private String extractUsername(Claims claims) {
        String preferred = claims.get("preferred_username", String.class);
        if (preferred != null && !preferred.isBlank()) return preferred;

        String email = claims.get("email", String.class);
        if (email != null && !email.isBlank()) return email;

        return claims.getSubject();
    }
}
