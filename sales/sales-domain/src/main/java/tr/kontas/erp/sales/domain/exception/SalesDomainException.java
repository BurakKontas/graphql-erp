package tr.kontas.erp.sales.domain.exception;

import tr.kontas.erp.core.kernel.exception.DomainException;

public abstract class SalesDomainException extends DomainException {

    protected SalesDomainException(String message) {
        super(message);
    }
}
