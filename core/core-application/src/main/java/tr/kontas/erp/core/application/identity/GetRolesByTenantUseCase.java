package tr.kontas.erp.core.application.identity;

import tr.kontas.erp.core.domain.identity.Role;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;

public interface GetRolesByTenantUseCase {
    List<Role> execute(TenantId tenantId);
}
