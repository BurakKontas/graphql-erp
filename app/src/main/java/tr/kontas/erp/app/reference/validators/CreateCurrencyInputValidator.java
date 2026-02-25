package tr.kontas.erp.app.reference.validators;

import tr.kontas.erp.app.reference.dtos.CreateCurrencyInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateCurrencyInputValidator extends Validator<CreateCurrencyInput> {
    public CreateCurrencyInputValidator() {
        ruleFor(CreateCurrencyInput::getCode).notNull().notBlank().minLength(3).maxLength(3);
        ruleFor(CreateCurrencyInput::getName).notNull().notBlank();
    }
}
