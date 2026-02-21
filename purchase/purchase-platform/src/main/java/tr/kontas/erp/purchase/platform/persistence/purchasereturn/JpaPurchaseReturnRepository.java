package tr.kontas.erp.purchase.platform.persistence.purchasereturn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPurchaseReturnRepository extends JpaRepository<PurchaseReturnJpaEntity, UUID> {
    Optional<PurchaseReturnJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<PurchaseReturnJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<PurchaseReturnJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.returnNumber, 8) AS int)), 0) FROM PurchaseReturnJpaEntity e WHERE e.tenantId = :tenantId")
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

