package tr.kontas.erp.finance.domain.exception;

public class InvalidCreditNoteStateException extends FinanceDomainException {
    public InvalidCreditNoteStateException(String currentStatus, String operation) {
        super("Cannot %s: credit note is in %s state".formatted(operation, currentStatus));
    }
}

