package tr.kontas.erp.app.inventory.validators;

import tr.kontas.erp.app.inventory.dtos.CreateItemInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateItemInputValidator extends Validator<CreateItemInput> {
    public CreateItemInputValidator() {
        ruleFor(CreateItemInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateItemInput::getCode).notNull().notBlank();
        ruleFor(CreateItemInput::getName).notNull().notBlank();
    }
}
