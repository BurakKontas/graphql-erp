package tr.kontas.erp.inventory.application.warehouse;

import tr.kontas.erp.inventory.domain.warehouse.Warehouse;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

public interface GetWarehouseByIdUseCase {
    Warehouse execute(WarehouseId id);
}
