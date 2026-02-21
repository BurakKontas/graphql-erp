package tr.kontas.erp.finance.domain.exception;

public class InvalidPaymentStateException extends FinanceDomainException {
    public InvalidPaymentStateException(String currentStatus, String operation) {
        super("Cannot %s: payment is in %s state".formatted(operation, currentStatus));
    }
}

