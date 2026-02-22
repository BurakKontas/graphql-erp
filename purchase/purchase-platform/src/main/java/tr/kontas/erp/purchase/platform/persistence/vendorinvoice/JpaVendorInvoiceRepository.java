package tr.kontas.erp.purchase.platform.persistence.vendorinvoice;

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
public interface JpaVendorInvoiceRepository extends JpaRepository<VendorInvoiceJpaEntity, UUID> {
    Optional<VendorInvoiceJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<VendorInvoiceJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<VendorInvoiceJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.invoiceNumber, 6) AS int)), 0) FROM VendorInvoiceJpaEntity e WHERE e.tenantId = :tenantId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

