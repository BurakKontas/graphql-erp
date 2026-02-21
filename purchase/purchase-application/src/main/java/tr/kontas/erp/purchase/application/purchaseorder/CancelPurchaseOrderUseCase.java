package tr.kontas.erp.purchase.application.purchaseorder;

public interface CancelPurchaseOrderUseCase {
    void cancel(String orderId, String reason);
}

