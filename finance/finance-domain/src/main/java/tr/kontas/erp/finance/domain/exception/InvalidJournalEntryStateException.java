package tr.kontas.erp.finance.domain.exception;

public class InvalidJournalEntryStateException extends FinanceDomainException {
    public InvalidJournalEntryStateException(String currentStatus, String operation) {
        super("Cannot %s: journal entry is in %s state".formatted(operation, currentStatus));
    }
}

