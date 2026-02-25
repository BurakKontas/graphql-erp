package tr.kontas.erp.app.crm.validators;

import tr.kontas.erp.app.crm.dtos.CreateActivityInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateActivityInputValidator extends Validator<CreateActivityInput> {
    public CreateActivityInputValidator() {
        ruleFor(CreateActivityInput::companyId).notNull().notBlank();
        ruleFor(CreateActivityInput::activityType).notNull().notBlank();
        ruleFor(CreateActivityInput::subject).notNull().notBlank();
    }
}
