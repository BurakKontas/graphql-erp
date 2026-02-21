package tr.kontas.erp.finance.domain.exception;

public class InvalidExpenseStateException extends FinanceDomainException {
    public InvalidExpenseStateException(String currentStatus, String operation) {
        super("Cannot %s: expense is in %s state".formatted(operation, currentStatus));
    }
}

