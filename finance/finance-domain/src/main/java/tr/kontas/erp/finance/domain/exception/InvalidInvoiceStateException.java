package tr.kontas.erp.finance.domain.exception;

public class InvalidInvoiceStateException extends FinanceDomainException {
    public InvalidInvoiceStateException(String currentStatus, String operation) {
        super("Cannot %s: invoice is in %s state".formatted(operation, currentStatus));
    }
}

