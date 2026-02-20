package tr.kontas.erp.core.platform.persistence.reference.tax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaTaxRepository extends JpaRepository<TaxJpaEntity, String> {
    Optional<TaxJpaEntity> findByTenantIdAndCompanyIdAndCode(UUID tenantId, UUID companyId, String code);

    List<TaxJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);

    List<TaxJpaEntity> findByTenantIdAndCompanyIdAndActiveTrue(UUID tenantId, UUID companyId);

    List<TaxJpaEntity> findByTenantIdAndCompanyIdIn(UUID tenantId, List<UUID> companyIds);

    List<TaxJpaEntity> findByCodeIn(List<String> codes);
}
