package tr.kontas.erp.core.platform.persistence.businesspartner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaBusinessPartnerRepository extends JpaRepository<BusinessPartnerJpaEntity, UUID> {
    Optional<BusinessPartnerJpaEntity> findByTenantIdAndCompanyIdAndCode(UUID tenantId, UUID companyId, String code);

    List<BusinessPartnerJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);

    List<BusinessPartnerJpaEntity> findByTenantId(UUID tenantId);

    List<BusinessPartnerJpaEntity> findByTenantIdAndCompanyIdIn(UUID tenantId, List<UUID> companyIds);

    List<BusinessPartnerJpaEntity> findByIdIn(List<UUID> ids);

    boolean existsByTenantIdAndCompanyIdAndCode(UUID tenantId, UUID companyId, String code);

    @Query("""
            select bp
            from BusinessPartnerJpaEntity bp
            where bp.tenantId = :tenantId
              and bp.companyId = :companyId
              and :role member of bp.roles
            """)
    List<BusinessPartnerJpaEntity> findByRole(UUID tenantId, UUID companyId, BusinessPartnerRole role);
}
