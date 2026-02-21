package tr.kontas.erp.inventory.domain.exception;

public class ItemNotActiveException extends InventoryDomainException {
    public ItemNotActiveException(String itemId) {
        super("Item '%s' is not active and cannot be used in stock operations".formatted(itemId));
    }
}