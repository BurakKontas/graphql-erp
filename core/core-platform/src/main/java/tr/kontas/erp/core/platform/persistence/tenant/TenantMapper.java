package tr.kontas.erp.core.platform.persistence.tenant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import tr.kontas.erp.core.domain.tenant.*;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Collections;
import java.util.List;

public final class TenantMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private TenantMapper() {}

    public static TenantJpaEntity toEntity(Tenant tenant) {
        TenantJpaEntity entity = new TenantJpaEntity();
        entity.setId(tenant.getId().asUUID());
        entity.setName(tenant.getName().getValue());
        entity.setCode(tenant.getCode().getValue());
        entity.setAuthMode(tenant.getAuthMode());

        // OIDC Settings
        if (tenant.getOidcSettings() != null) {
            OidcSettings oidc = tenant.getOidcSettings();
            entity.setOidcIssuer(oidc.getIssuer());
            entity.setOidcAudience(oidc.getAudience());
            entity.setOidcJwkSetUri(oidc.getJwkSetUri());
            entity.setOidcClockSkewSeconds(oidc.getClockSkewSeconds());
        }

        // LDAP Settings
        if (tenant.getLdapSettings() != null) {
            LdapSettings ldap = tenant.getLdapSettings();
            entity.setLdapUrls(toJson(ldap.getUrls()));
            entity.setLdapBaseDn(ldap.getBaseDn());
            entity.setLdapUserSearchFilter(ldap.getUserSearchFilter());
            entity.setLdapBindDn(ldap.getBindDn());
            entity.setLdapBindPassword(ldap.getBindPassword());
            entity.setLdapConnectTimeoutMs(ldap.getConnectTimeoutMs());
            entity.setLdapReadTimeoutMs(ldap.getReadTimeoutMs());
            entity.setLdapStartTls(ldap.isStartTls());
            entity.setLdapMaxRetry(ldap.getMaxRetry());
            entity.setLdapCircuitBreakerTimeoutMs(ldap.getCircuitBreakerTimeoutMs());
            entity.setLdapResolveGroups(ldap.isResolveGroups());
            entity.setLdapGroupSearchFilter(ldap.getGroupSearchFilter());
        }

        entity.setAuditUnauthenticated(tenant.isAuditUnauthenticated());

        return entity;
    }

    public static Tenant toDomain(TenantJpaEntity entity) {
        OidcSettings oidcSettings = null;
        if (entity.getOidcIssuer() != null) {
            oidcSettings = OidcSettings.builder()
                    .issuer(entity.getOidcIssuer())
                    .audience(entity.getOidcAudience())
                    .jwkSetUri(entity.getOidcJwkSetUri())
                    .clockSkewSeconds(entity.getOidcClockSkewSeconds() != null ? entity.getOidcClockSkewSeconds() : 60L)
                    .build();
        }

        LdapSettings ldapSettings = null;
        if (entity.getLdapBaseDn() != null) {
            ldapSettings = LdapSettings.builder()
                    .urls(fromJson(entity.getLdapUrls()))
                    .baseDn(entity.getLdapBaseDn())
                    .userSearchFilter(entity.getLdapUserSearchFilter())
                    .bindDn(entity.getLdapBindDn())
                    .bindPassword(entity.getLdapBindPassword())
                    .connectTimeoutMs(entity.getLdapConnectTimeoutMs() != null ? entity.getLdapConnectTimeoutMs() : 5000)
                    .readTimeoutMs(entity.getLdapReadTimeoutMs() != null ? entity.getLdapReadTimeoutMs() : 5000)
                    .startTls(entity.getLdapStartTls() != null && entity.getLdapStartTls())
                    .maxRetry(entity.getLdapMaxRetry() != null ? entity.getLdapMaxRetry() : 2)
                    .circuitBreakerTimeoutMs(entity.getLdapCircuitBreakerTimeoutMs() != null ? entity.getLdapCircuitBreakerTimeoutMs() : 30000L)
                    .resolveGroups(entity.getLdapResolveGroups() != null && entity.getLdapResolveGroups())
                    .groupSearchFilter(entity.getLdapGroupSearchFilter() != null ? entity.getLdapGroupSearchFilter() : "(member={0})")
                    .build();
        }

        return new Tenant(
                TenantId.of(entity.getId()),
                new TenantName(entity.getName()),
                new TenantCode(entity.getCode()),
                entity.getAuthMode(),
                oidcSettings,
                ldapSettings,
                true,
                entity.isAuditUnauthenticated()
        );
    }

    private static String toJson(List<String> list) {
        if (list == null) return null;
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private static List<String> fromJson(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }
}