package tr.kontas.erp.purchase.platform.persistence.purchaserequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPurchaseRequestRepository extends JpaRepository<PurchaseRequestJpaEntity, UUID> {
    Optional<PurchaseRequestJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<PurchaseRequestJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<PurchaseRequestJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.requestNumber, 4) AS int)), 0) FROM PurchaseRequestJpaEntity e WHERE e.tenantId = :tenantId")
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

