package tr.kontas.erp.inventory.platform.persistence.stockmovement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.stockmovement.*;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StockMovementRepositoryImpl implements StockMovementRepository {

    private final JpaStockMovementRepository jpaRepository;

    @Override
    public void save(StockMovement movement) {
        jpaRepository.save(StockMovementMapper.toEntity(movement));
    }

    @Override
    public Optional<StockMovement> findById(StockMovementId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(StockMovementMapper::toDomain);
    }

    @Override
    public List<StockMovement> findByWarehouseId(WarehouseId warehouseId, TenantId tenantId) {
        return jpaRepository.findByWarehouseIdAndTenantId(warehouseId.asUUID(), tenantId.asUUID())
                .stream()
                .map(StockMovementMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovement> findByItemId(ItemId itemId, TenantId tenantId) {
        return jpaRepository.findByItemIdAndTenantId(itemId.asUUID(), tenantId.asUUID())
                .stream()
                .map(StockMovementMapper::toDomain)
                .collect(Collectors.toList());
    }
}
