package tr.kontas.erp.inventory.application.stocklevel;

import tr.kontas.erp.inventory.domain.stocklevel.StockLevel;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.List;

public interface GetStockLevelsByWarehouseUseCase {
    List<StockLevel> execute(WarehouseId warehouseId);
}
