package tr.kontas.erp.purchase.application.purchasereturn;

import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturn;
import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturnId;

import java.util.List;

public interface GetPurchaseReturnsByIdsUseCase {
    List<PurchaseReturn> execute(List<PurchaseReturnId> ids);
}

