package tr.kontas.erp.core.application.tenant;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public interface CreateTenantUseCase {
    TenantId execute(CreateTenantCommand command);
}