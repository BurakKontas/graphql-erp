package tr.kontas.erp.inventory.platform.persistence.stockmovement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaStockMovementRepository extends JpaRepository<StockMovementJpaEntity, UUID> {
    Optional<StockMovementJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<StockMovementJpaEntity> findByWarehouseIdAndTenantId(UUID warehouseId, UUID tenantId);
    List<StockMovementJpaEntity> findByItemIdAndTenantId(UUID itemId, UUID tenantId);
}
