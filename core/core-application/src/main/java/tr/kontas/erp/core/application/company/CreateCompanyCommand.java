package tr.kontas.erp.core.application.company;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public record CreateCompanyCommand(
        TenantId tenantId,
        String name
) {}