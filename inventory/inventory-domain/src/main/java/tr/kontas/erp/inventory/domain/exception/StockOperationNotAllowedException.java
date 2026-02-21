package tr.kontas.erp.inventory.domain.exception;

public class StockOperationNotAllowedException extends InventoryDomainException {
    public StockOperationNotAllowedException(String message) {
        super(message);
    }
}
