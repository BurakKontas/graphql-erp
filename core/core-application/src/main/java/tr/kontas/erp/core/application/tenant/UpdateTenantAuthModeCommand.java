package tr.kontas.erp.core.application.tenant;

import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;

public record UpdateTenantAuthModeCommand(
        String tenantId,
        AuthProviderType authMode
) {
}
