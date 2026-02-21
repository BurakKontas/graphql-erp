package tr.kontas.erp.inventory.platform.persistence.stocklevel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaStockLevelRepository extends JpaRepository<StockLevelJpaEntity, UUID> {
    Optional<StockLevelJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<StockLevelJpaEntity> findByItemIdAndWarehouseIdAndTenantId(UUID itemId, UUID warehouseId, UUID tenantId);
    List<StockLevelJpaEntity> findByWarehouseIdAndTenantId(UUID warehouseId, UUID tenantId);
    List<StockLevelJpaEntity> findByItemIdAndTenantId(UUID itemId, UUID tenantId);
}
