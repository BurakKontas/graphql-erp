package tr.kontas.erp.purchase.application.purchaseorder;

import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrder;
import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrderId;

public interface GetPurchaseOrderByIdUseCase {
    PurchaseOrder execute(PurchaseOrderId id);
}

