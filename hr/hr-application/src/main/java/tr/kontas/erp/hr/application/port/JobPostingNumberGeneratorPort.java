package tr.kontas.erp.hr.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.jobposting.JobPostingNumber;

public interface JobPostingNumberGeneratorPort {
    JobPostingNumber generate(TenantId tenantId, CompanyId companyId, int year);
}
