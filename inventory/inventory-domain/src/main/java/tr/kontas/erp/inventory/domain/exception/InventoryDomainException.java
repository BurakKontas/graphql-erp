package tr.kontas.erp.inventory.domain.exception;

import tr.kontas.erp.core.kernel.exception.DomainException;

public abstract class InventoryDomainException extends DomainException {
    protected InventoryDomainException(String message) {
        super(message);
    }
}