package tr.kontas.erp.finance.domain.exception;

public abstract class FinanceDomainException extends RuntimeException {

    protected FinanceDomainException(String message) {
        super(message);
    }

    protected FinanceDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

