package tr.kontas.erp.hr.application.jobapplication;

import tr.kontas.erp.hr.domain.jobapplication.JobApplication;
import tr.kontas.erp.hr.domain.jobapplication.JobApplicationId;

public interface GetJobApplicationByIdUseCase {
    JobApplication execute(JobApplicationId id);
}
