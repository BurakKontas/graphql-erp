package tr.kontas.erp.purchase.platform.persistence.purchaseorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPurchaseOrderRepository extends JpaRepository<PurchaseOrderJpaEntity, UUID> {
    Optional<PurchaseOrderJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<PurchaseOrderJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<PurchaseOrderJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.orderNumber, 4) AS int)), 0) FROM PurchaseOrderJpaEntity e WHERE e.tenantId = :tenantId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

