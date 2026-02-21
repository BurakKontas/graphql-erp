package tr.kontas.erp.finance.domain.exception;

public class InvalidAccountStateException extends FinanceDomainException {
    public InvalidAccountStateException(String message) {
        super(message);
    }
}

