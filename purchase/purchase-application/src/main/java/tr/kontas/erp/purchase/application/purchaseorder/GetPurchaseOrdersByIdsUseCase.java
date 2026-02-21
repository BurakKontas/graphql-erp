package tr.kontas.erp.purchase.application.purchaseorder;

import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrder;
import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrderId;

import java.util.List;

public interface GetPurchaseOrdersByIdsUseCase {
    List<PurchaseOrder> execute(List<PurchaseOrderId> ids);
}

