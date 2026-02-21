package tr.kontas.erp.finance.domain.exception;

public class InvalidPeriodStateException extends FinanceDomainException {
    public InvalidPeriodStateException(String currentStatus, String operation) {
        super("Cannot %s: period is in %s state".formatted(operation, currentStatus));
    }
}

