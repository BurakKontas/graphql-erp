package tr.kontas.erp.finance.platform.persistence.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPaymentRepository extends JpaRepository<PaymentJpaEntity, UUID> {
    Optional<PaymentJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<PaymentJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<PaymentJpaEntity> findByTenantIdAndInvoiceId(UUID tenantId, String invoiceId);
    List<PaymentJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.paymentNumber, 10) AS int)), 0) FROM PaymentJpaEntity e WHERE e.tenantId = :tenantId")
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

