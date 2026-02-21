package tr.kontas.erp.sales.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public interface ItemValidationPort {
    void validateItemExists(TenantId tenantId, CompanyId companyId, String itemId);

    boolean exists(TenantId tenantId, CompanyId companyId, String itemId);
}
