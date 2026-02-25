package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreatePositionInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreatePositionInputValidator extends Validator<CreatePositionInput> {
    public CreatePositionInputValidator() {
        ruleFor(CreatePositionInput::getCompanyId).notNull().notBlank();
        ruleFor(CreatePositionInput::getCode).notNull().notBlank();
        ruleFor(CreatePositionInput::getTitle).notNull().notBlank();
    }
}
