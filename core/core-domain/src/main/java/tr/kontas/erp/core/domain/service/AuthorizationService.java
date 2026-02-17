package tr.kontas.erp.core.domain.service;

import tr.kontas.erp.core.domain.identity.valueobjects.PermissionKey;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public interface AuthorizationService {
    boolean hasPermission(UserId userId, TenantId tenantId, PermissionKey permissionKey);
}
