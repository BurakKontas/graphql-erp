package tr.kontas.erp.purchase.application.purchasereturn;

import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturnId;

public interface CreatePurchaseReturnUseCase {
    PurchaseReturnId execute(CreatePurchaseReturnCommand command);
}

