package tr.kontas.erp.core.application.tenant;

import tr.kontas.erp.core.domain.tenant.OidcSettings;

public record UpdateTenantOidcSettingsCommand(
        String tenantId,
        OidcSettings oidcSettings
) {
}
