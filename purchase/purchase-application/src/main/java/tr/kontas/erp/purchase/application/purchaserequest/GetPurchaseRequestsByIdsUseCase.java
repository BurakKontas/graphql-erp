package tr.kontas.erp.purchase.application.purchaserequest;

import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequest;
import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequestId;

import java.util.List;

public interface GetPurchaseRequestsByIdsUseCase {
    List<PurchaseRequest> execute(List<PurchaseRequestId> ids);
}

