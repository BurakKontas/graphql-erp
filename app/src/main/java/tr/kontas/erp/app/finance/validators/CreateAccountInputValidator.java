package tr.kontas.erp.app.finance.validators;

import tr.kontas.erp.app.finance.dtos.CreateAccountInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateAccountInputValidator extends Validator<CreateAccountInput> {
    public CreateAccountInputValidator() {
        ruleFor(CreateAccountInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateAccountInput::getCode).notNull().notBlank();
        ruleFor(CreateAccountInput::getName).notNull().notBlank();
        ruleFor(CreateAccountInput::getType).notNull().notBlank();
        ruleFor(CreateAccountInput::getNature).notNull().notBlank();
    }
}
