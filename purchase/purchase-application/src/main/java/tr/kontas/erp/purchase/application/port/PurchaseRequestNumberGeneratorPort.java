package tr.kontas.erp.purchase.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequestNumber;

public interface PurchaseRequestNumberGeneratorPort {
    PurchaseRequestNumber generate(TenantId tenantId, CompanyId companyId, int year);
}

