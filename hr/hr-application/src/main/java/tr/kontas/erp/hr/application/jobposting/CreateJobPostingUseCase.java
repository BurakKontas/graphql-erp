package tr.kontas.erp.hr.application.jobposting;

import tr.kontas.erp.hr.domain.jobposting.JobPostingId;

public interface CreateJobPostingUseCase {
    JobPostingId execute(CreateJobPostingCommand command);
}
