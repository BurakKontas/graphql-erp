package tr.kontas.erp.hr.platform.persistence.performancereview;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaPerformanceReviewRepository extends JpaRepository<PerformanceReviewJpaEntity, UUID> {
    List<PerformanceReviewJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<PerformanceReviewJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
