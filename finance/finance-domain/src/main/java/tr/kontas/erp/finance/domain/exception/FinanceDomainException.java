package tr.kontas.erp.finance.domain.exception;

import tr.kontas.erp.core.kernel.exception.DomainException;

public abstract class FinanceDomainException extends DomainException {

    protected FinanceDomainException(String message) {
        super(message);
    }
}

