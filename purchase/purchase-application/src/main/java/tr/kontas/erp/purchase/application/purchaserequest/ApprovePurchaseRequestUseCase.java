package tr.kontas.erp.purchase.application.purchaserequest;

public interface ApprovePurchaseRequestUseCase {
    void approve(String requestId, String approverId);
}

