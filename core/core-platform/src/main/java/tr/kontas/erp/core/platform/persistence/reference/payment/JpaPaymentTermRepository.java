package tr.kontas.erp.core.platform.persistence.reference.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPaymentTermRepository extends JpaRepository<PaymentTermJpaEntity, String> {
    Optional<PaymentTermJpaEntity> findByTenantIdAndCompanyIdAndCode(UUID tenantId, UUID companyId, String code);

    List<PaymentTermJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);

    List<PaymentTermJpaEntity> findByTenantIdAndCompanyIdIn(UUID tenantId, List<UUID> companyIds);

    List<PaymentTermJpaEntity> findByCodeIn(List<String> codes);
}
