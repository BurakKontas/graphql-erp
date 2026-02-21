package tr.kontas.erp.purchase.application.purchaserequest;

import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequestId;

public interface CreatePurchaseRequestUseCase {
    PurchaseRequestId execute(CreatePurchaseRequestCommand command);
}

