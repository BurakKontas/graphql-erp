package tr.kontas.erp.core.application.tenant;

import tr.kontas.erp.core.domain.tenant.LdapSettings;

public record UpdateTenantLdapSettingsCommand(
        String tenantId,
        LdapSettings ldapSettings
) {
}
