package tr.kontas.erp.core.application.tenant;

import tr.kontas.erp.core.domain.tenant.Tenant;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Optional;

public interface GetTenantByIdUseCase {
    Optional<Tenant> execute(TenantId id);
}
