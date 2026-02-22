package tr.kontas.erp.shipment.platform.persistence.shipment;

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
public interface JpaShipmentRepository extends JpaRepository<ShipmentJpaEntity, UUID> {
    Optional<ShipmentJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<ShipmentJpaEntity> findByNumberAndTenantId(String number, UUID tenantId);
    boolean existsByNumberAndTenantId(String number, UUID tenantId);
    List<ShipmentJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<ShipmentJpaEntity> findByDeliveryOrderIdAndTenantId(String deliveryOrderId, UUID tenantId);
    List<ShipmentJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.number, 9) AS int)), 0) FROM ShipmentJpaEntity e WHERE e.tenantId = :tenantId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

