package tr.kontas.erp.hr.domain.jobapplication;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository {
    void save(JobApplication entity);
    Optional<JobApplication> findById(JobApplicationId id, TenantId tenantId);
    List<JobApplication> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<JobApplication> findByIds(List<JobApplicationId> ids);
}
