package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreateJobPostingInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateJobPostingInputValidator extends Validator<CreateJobPostingInput> {
    public CreateJobPostingInputValidator() {
        ruleFor(CreateJobPostingInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateJobPostingInput::getPositionId).notNull().notBlank();
        ruleFor(CreateJobPostingInput::getTitle).notNull().notBlank();
    }
}
