package tr.kontas.erp.hr.application.jobposting;

import tr.kontas.erp.hr.domain.jobposting.JobPosting;
import tr.kontas.erp.hr.domain.jobposting.JobPostingId;

public interface GetJobPostingByIdUseCase {
    JobPosting execute(JobPostingId id);
}
