package tr.kontas.erp.hr.application.jobapplication;

import tr.kontas.erp.hr.domain.jobapplication.JobApplicationId;

public interface CreateJobApplicationUseCase {
    JobApplicationId execute(CreateJobApplicationCommand command);
}
