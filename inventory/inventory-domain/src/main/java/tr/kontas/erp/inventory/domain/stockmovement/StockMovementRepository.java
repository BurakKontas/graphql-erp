package tr.kontas.erp.inventory.domain.stockmovement;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.List;
import java.util.Optional;

public interface StockMovementRepository {
    void save(StockMovement movement);
    Optional<StockMovement> findById(StockMovementId id, TenantId tenantId);
    List<StockMovement> findByWarehouseId(WarehouseId warehouseId, TenantId tenantId);
    List<StockMovement> findByItemId(ItemId itemId, TenantId tenantId);
}
