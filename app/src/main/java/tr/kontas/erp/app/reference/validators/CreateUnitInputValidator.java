package tr.kontas.erp.app.reference.validators;

import tr.kontas.erp.app.reference.dtos.CreateUnitInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateUnitInputValidator extends Validator<CreateUnitInput> {
    public CreateUnitInputValidator() {
        ruleFor(CreateUnitInput::getCode).notNull().notBlank();
        ruleFor(CreateUnitInput::getName).notNull().notBlank();
    }
}
