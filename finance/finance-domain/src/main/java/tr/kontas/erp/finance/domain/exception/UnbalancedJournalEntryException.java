package tr.kontas.erp.finance.domain.exception;

public class UnbalancedJournalEntryException extends FinanceDomainException {
    public UnbalancedJournalEntryException(String message) {
        super(message);
    }
}

