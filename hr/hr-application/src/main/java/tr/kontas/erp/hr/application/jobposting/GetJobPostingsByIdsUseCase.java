package tr.kontas.erp.hr.application.jobposting;

import tr.kontas.erp.hr.domain.jobposting.JobPosting;
import tr.kontas.erp.hr.domain.jobposting.JobPostingId;

import java.util.List;

public interface GetJobPostingsByIdsUseCase {
    List<JobPosting> execute(List<JobPostingId> ids);
}
