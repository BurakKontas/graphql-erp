package tr.kontas.erp.purchase.application.purchaserequest;

public interface RejectPurchaseRequestUseCase {
    void reject(String requestId, String approverId, String reason);
}

