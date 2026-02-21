package tr.kontas.erp.inventory.application.stockmovement;

import tr.kontas.erp.inventory.domain.stockmovement.StockMovement;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.List;

public interface GetStockMovementsByWarehouseUseCase {
    List<StockMovement> execute(WarehouseId warehouseId);
}
