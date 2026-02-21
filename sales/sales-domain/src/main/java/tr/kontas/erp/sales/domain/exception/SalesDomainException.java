package tr.kontas.erp.sales.domain.exception;

public abstract class SalesDomainException extends RuntimeException {

    protected SalesDomainException(String message) {
        super(message);
    }

    protected SalesDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
