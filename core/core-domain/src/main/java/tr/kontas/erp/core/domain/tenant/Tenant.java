package tr.kontas.erp.core.domain.tenant;

import lombok.Getter;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Getter
public class Tenant extends AggregateRoot<TenantId> {
    private final TenantName name;
    private final TenantCode code;
    private AuthProviderType authMode;
    private OidcSettings oidcSettings;
    private LdapSettings ldapSettings;
    private boolean active;

    public Tenant(TenantId id, TenantName name, TenantCode code) {
        this(id, name, code, AuthProviderType.LOCAL, null, null);
    }

    public Tenant(TenantId id, TenantName name, TenantCode code, AuthProviderType authMode) {
        this(id, name, code, authMode, null, null);
    }

    public Tenant(TenantId id, TenantName name, TenantCode code, AuthProviderType authMode,
                  OidcSettings oidcSettings, LdapSettings ldapSettings) {
        super(id);
        this.name = name;
        this.code = code;
        this.authMode = authMode != null ? authMode : AuthProviderType.LOCAL;
        this.oidcSettings = oidcSettings;
        this.ldapSettings = ldapSettings;
        this.active = true;

        registerEvent(new TenantCreatedEvent(id));
    }

    public void changeAuthMode(AuthProviderType authMode) {
        this.authMode = authMode != null ? authMode : AuthProviderType.LOCAL;
    }

    public void updateOidcSettings(OidcSettings oidcSettings) {
        this.oidcSettings = oidcSettings;
    }

    public void updateLdapSettings(LdapSettings ldapSettings) {
        this.ldapSettings = ldapSettings;
    }

    public void deactivate() {
        this.active = false;
    }

}
