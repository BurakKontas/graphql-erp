package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreatePerformanceCycleInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreatePerformanceCycleInputValidator extends Validator<CreatePerformanceCycleInput> {
    public CreatePerformanceCycleInputValidator() {
        ruleFor(CreatePerformanceCycleInput::getCompanyId).notNull().notBlank();
        ruleFor(CreatePerformanceCycleInput::getName).notNull().notBlank();
    }
}
