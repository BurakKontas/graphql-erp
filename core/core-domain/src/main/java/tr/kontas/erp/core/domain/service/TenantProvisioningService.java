package tr.kontas.erp.core.domain.service;

import tr.kontas.erp.core.domain.tenant.Tenant;

public interface TenantProvisioningService {
    void provision(Tenant tenant);
}
