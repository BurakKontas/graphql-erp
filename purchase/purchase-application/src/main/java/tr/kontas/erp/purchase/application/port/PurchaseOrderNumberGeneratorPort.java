package tr.kontas.erp.purchase.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrderNumber;

public interface PurchaseOrderNumberGeneratorPort {
    PurchaseOrderNumber generate(TenantId tenantId, CompanyId companyId, int year);
}

