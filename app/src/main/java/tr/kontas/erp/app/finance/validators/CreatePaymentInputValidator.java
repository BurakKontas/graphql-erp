package tr.kontas.erp.app.finance.validators;

import tr.kontas.erp.app.finance.dtos.CreatePaymentInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreatePaymentInputValidator extends Validator<CreatePaymentInput> {
    public CreatePaymentInputValidator() {
        ruleFor(CreatePaymentInput::getCompanyId).notNull().notBlank();
        ruleFor(CreatePaymentInput::getPaymentDate).notNull().notBlank();
        ruleFor(CreatePaymentInput::getAmount).notNull();
    }
}
