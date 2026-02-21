package tr.kontas.erp.hr.application.jobapplication;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.jobapplication.JobApplication;

import java.util.List;

public interface GetJobApplicationsByCompanyUseCase {
    List<JobApplication> execute(CompanyId companyId);
}
