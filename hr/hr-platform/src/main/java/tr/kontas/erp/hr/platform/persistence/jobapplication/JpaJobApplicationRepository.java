package tr.kontas.erp.hr.platform.persistence.jobapplication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaJobApplicationRepository extends JpaRepository<JobApplicationJpaEntity, UUID> {
    List<JobApplicationJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<JobApplicationJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
