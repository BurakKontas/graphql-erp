package tr.kontas.erp.purchase.application.purchaseorder;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrder;

import java.util.List;

public interface GetPurchaseOrdersByCompanyUseCase {
    List<PurchaseOrder> execute(CompanyId companyId);
}

