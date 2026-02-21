package tr.kontas.erp.hr.application.jobposting;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.jobposting.JobPosting;

import java.util.List;

public interface GetJobPostingsByCompanyUseCase {
    List<JobPosting> execute(CompanyId companyId);
}
