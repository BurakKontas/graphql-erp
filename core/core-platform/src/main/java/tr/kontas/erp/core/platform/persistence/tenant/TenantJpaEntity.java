package tr.kontas.erp.core.platform.persistence.tenant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;

import java.util.UUID;

@Entity
@Table(name = "tenants", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_mode", nullable = false)
    private AuthProviderType authMode = AuthProviderType.LOCAL;

    // OIDC Settings
    @Column(name = "oidc_issuer")
    private String oidcIssuer;

    @Column(name = "oidc_audience")
    private String oidcAudience;

    @Column(name = "oidc_jwk_set_uri")
    private String oidcJwkSetUri;

    @Column(name = "oidc_clock_skew_seconds")
    private Long oidcClockSkewSeconds = 60L;

    // LDAP Settings
    @Column(name = "ldap_urls")
    private String ldapUrls; // JSON array stored as string

    @Column(name = "ldap_base_dn")
    private String ldapBaseDn;

    @Column(name = "ldap_user_search_filter")
    private String ldapUserSearchFilter;

    @Column(name = "ldap_bind_dn")
    private String ldapBindDn;

    @Column(name = "ldap_bind_password")
    private String ldapBindPassword;

    @Column(name = "ldap_connect_timeout_ms")
    private Integer ldapConnectTimeoutMs = 5000;

    @Column(name = "ldap_read_timeout_ms")
    private Integer ldapReadTimeoutMs = 5000;

    @Column(name = "ldap_start_tls")
    private Boolean ldapStartTls = false;

    @Column(name = "ldap_max_retry")
    private Integer ldapMaxRetry = 2;

    @Column(name = "ldap_circuit_breaker_timeout_ms")
    private Long ldapCircuitBreakerTimeoutMs = 30000L;

    @Column(name = "ldap_resolve_groups")
    private Boolean ldapResolveGroups = false;

    @Column(name = "ldap_group_search_filter")
    private String ldapGroupSearchFilter = "(member={0})";
}
