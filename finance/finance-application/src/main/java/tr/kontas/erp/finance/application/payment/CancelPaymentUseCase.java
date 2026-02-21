package tr.kontas.erp.finance.application.payment;

public interface CancelPaymentUseCase {
    void execute(String paymentId, String reason);
}

