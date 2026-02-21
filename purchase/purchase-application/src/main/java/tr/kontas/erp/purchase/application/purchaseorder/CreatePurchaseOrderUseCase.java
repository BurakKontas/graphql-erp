package tr.kontas.erp.purchase.application.purchaseorder;

import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrderId;

public interface CreatePurchaseOrderUseCase {
    PurchaseOrderId execute(CreatePurchaseOrderCommand command);
}

