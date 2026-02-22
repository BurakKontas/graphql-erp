package tr.kontas.erp.shipment.platform.persistence.deliveryorder;

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
public interface JpaDeliveryOrderRepository extends JpaRepository<DeliveryOrderJpaEntity, UUID> {
    Optional<DeliveryOrderJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<DeliveryOrderJpaEntity> findByNumberAndTenantId(String number, UUID tenantId);
    boolean existsByNumberAndTenantId(String number, UUID tenantId);
    List<DeliveryOrderJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<DeliveryOrderJpaEntity> findBySalesOrderIdAndTenantId(String salesOrderId, UUID tenantId);
    List<DeliveryOrderJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.number, 9) AS int)), 0) FROM DeliveryOrderJpaEntity e WHERE e.tenantId = :tenantId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

