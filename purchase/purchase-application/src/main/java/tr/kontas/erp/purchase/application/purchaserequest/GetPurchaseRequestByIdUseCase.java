package tr.kontas.erp.purchase.application.purchaserequest;

import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequest;
import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequestId;

public interface GetPurchaseRequestByIdUseCase {
    PurchaseRequest execute(PurchaseRequestId id);
}

