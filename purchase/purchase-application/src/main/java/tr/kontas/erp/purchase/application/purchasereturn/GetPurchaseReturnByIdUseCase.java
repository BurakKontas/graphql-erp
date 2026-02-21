package tr.kontas.erp.purchase.application.purchasereturn;

import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturn;
import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturnId;

public interface GetPurchaseReturnByIdUseCase {
    PurchaseReturn execute(PurchaseReturnId id);
}

