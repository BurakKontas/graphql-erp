package tr.kontas.erp.finance.platform.persistence.salesinvoice;

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
public interface JpaSalesInvoiceRepository extends JpaRepository<SalesInvoiceJpaEntity, UUID> {
    Optional<SalesInvoiceJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<SalesInvoiceJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    Optional<SalesInvoiceJpaEntity> findByTenantIdAndSalesOrderId(UUID tenantId, String salesOrderId);
    List<SalesInvoiceJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.invoiceNumber, 10) AS int)), 0) FROM SalesInvoiceJpaEntity e WHERE e.tenantId = :tenantId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

