package tr.kontas.erp.inventory.domain.stocklevel;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.List;
import java.util.Optional;

public interface StockLevelRepository {
    void save(StockLevel stockLevel);
    Optional<StockLevel> findById(StockLevelId id, TenantId tenantId);
    Optional<StockLevel> findByItemAndWarehouse(ItemId itemId, WarehouseId warehouseId, TenantId tenantId);
    List<StockLevel> findByWarehouseId(WarehouseId warehouseId, TenantId tenantId);
    List<StockLevel> findByItemId(ItemId itemId, TenantId tenantId);
}
