package tr.kontas.erp.shipment.platform.persistence.shipmentreturn;

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
public interface JpaShipmentReturnRepository extends JpaRepository<ShipmentReturnJpaEntity, UUID> {
    Optional<ShipmentReturnJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<ShipmentReturnJpaEntity> findByNumberAndTenantId(String number, UUID tenantId);
    boolean existsByNumberAndTenantId(String number, UUID tenantId);
    List<ShipmentReturnJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<ShipmentReturnJpaEntity> findByShipmentIdAndTenantId(String shipmentId, UUID tenantId);
    List<ShipmentReturnJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.number, 9) AS int)), 0) FROM ShipmentReturnJpaEntity e WHERE e.tenantId = :tenantId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

