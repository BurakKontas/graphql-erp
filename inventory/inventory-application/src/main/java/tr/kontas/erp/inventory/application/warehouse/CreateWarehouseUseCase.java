package tr.kontas.erp.inventory.application.warehouse;

import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

public interface CreateWarehouseUseCase {
    WarehouseId execute(CreateWarehouseCommand command);
}
