package tr.kontas.erp.hr.domain.jobposting;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface JobPostingRepository {
    void save(JobPosting entity);
    Optional<JobPosting> findById(JobPostingId id, TenantId tenantId);
    List<JobPosting> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<JobPosting> findByIds(List<JobPostingId> ids);
}
