package tr.kontas.erp.hr.platform.persistence.jobposting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaJobPostingRepository extends JpaRepository<JobPostingJpaEntity, UUID> {
    List<JobPostingJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<JobPostingJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
