package tr.kontas.erp.app.crm.validators;

import tr.kontas.erp.app.crm.dtos.CreateQuoteInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateQuoteInputValidator extends Validator<CreateQuoteInput> {
    public CreateQuoteInputValidator() {
        ruleFor(CreateQuoteInput::companyId).notNull().notBlank();
        ruleFor(CreateQuoteInput::quoteDate).notNull().notBlank();
    }
}
