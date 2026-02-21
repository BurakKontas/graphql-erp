package tr.kontas.erp.inventory.domain.exception;

public abstract class InventoryDomainException extends RuntimeException {
    protected InventoryDomainException(String message) {
        super(message);
    }
}