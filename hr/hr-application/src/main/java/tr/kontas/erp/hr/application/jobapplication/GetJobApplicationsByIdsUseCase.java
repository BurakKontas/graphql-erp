package tr.kontas.erp.hr.application.jobapplication;

import tr.kontas.erp.hr.domain.jobapplication.JobApplication;
import tr.kontas.erp.hr.domain.jobapplication.JobApplicationId;

import java.util.List;

public interface GetJobApplicationsByIdsUseCase {
    List<JobApplication> execute(List<JobApplicationId> ids);
}
