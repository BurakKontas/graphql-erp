package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreateJobApplicationInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateJobApplicationInputValidator extends Validator<CreateJobApplicationInput> {
    public CreateJobApplicationInputValidator() {
        ruleFor(CreateJobApplicationInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateJobApplicationInput::getJobPostingId).notNull().notBlank();
        ruleFor(CreateJobApplicationInput::getFirstName).notNull().notBlank();
        ruleFor(CreateJobApplicationInput::getLastName).notNull().notBlank();
    }
}
