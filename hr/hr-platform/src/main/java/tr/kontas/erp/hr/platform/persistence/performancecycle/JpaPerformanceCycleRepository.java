package tr.kontas.erp.hr.platform.persistence.performancecycle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaPerformanceCycleRepository extends JpaRepository<PerformanceCycleJpaEntity, UUID> {
    List<PerformanceCycleJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<PerformanceCycleJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
