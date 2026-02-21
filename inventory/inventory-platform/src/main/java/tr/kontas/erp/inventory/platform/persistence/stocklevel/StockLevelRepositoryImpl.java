package tr.kontas.erp.inventory.platform.persistence.stocklevel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.stocklevel.*;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StockLevelRepositoryImpl implements StockLevelRepository {

    private final JpaStockLevelRepository jpaRepository;

    @Override
    public void save(StockLevel stockLevel) {
        jpaRepository.save(StockLevelMapper.toEntity(stockLevel));
    }

    @Override
    public Optional<StockLevel> findById(StockLevelId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(StockLevelMapper::toDomain);
    }

    @Override
    public Optional<StockLevel> findByItemAndWarehouse(ItemId itemId, WarehouseId warehouseId, TenantId tenantId) {
        return jpaRepository.findByItemIdAndWarehouseIdAndTenantId(itemId.asUUID(), warehouseId.asUUID(), tenantId.asUUID())
                .map(StockLevelMapper::toDomain);
    }

    @Override
    public List<StockLevel> findByWarehouseId(WarehouseId warehouseId, TenantId tenantId) {
        return jpaRepository.findByWarehouseIdAndTenantId(warehouseId.asUUID(), tenantId.asUUID())
                .stream()
                .map(StockLevelMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockLevel> findByItemId(ItemId itemId, TenantId tenantId) {
        return jpaRepository.findByItemIdAndTenantId(itemId.asUUID(), tenantId.asUUID())
                .stream()
                .map(StockLevelMapper::toDomain)
                .collect(Collectors.toList());
    }
}
