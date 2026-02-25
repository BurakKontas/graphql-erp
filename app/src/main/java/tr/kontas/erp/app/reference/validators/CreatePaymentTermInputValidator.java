package tr.kontas.erp.app.reference.validators;

import tr.kontas.erp.app.reference.dtos.CreatePaymentTermInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreatePaymentTermInputValidator extends Validator<CreatePaymentTermInput> {
    public CreatePaymentTermInputValidator() {
        ruleFor(CreatePaymentTermInput::getCompanyId).notNull().notBlank();
        ruleFor(CreatePaymentTermInput::getCode).notNull().notBlank();
        ruleFor(CreatePaymentTermInput::getName).notNull().notBlank();
        ruleFor(CreatePaymentTermInput::getDueDays).greaterThanOrEqualTo(0);
    }
}
