package tr.kontas.erp.inventory.application.warehouse;

import tr.kontas.erp.inventory.domain.warehouse.Warehouse;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.List;

public interface GetWarehousesByIdsUseCase {
    List<Warehouse> execute(List<WarehouseId> ids);
}
