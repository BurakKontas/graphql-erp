package tr.kontas.erp.inventory.application.warehouse;

public interface RenameWarehouseUseCase {
    void execute(String warehouseId, String newName);
}
