package tr.kontas.erp.app.crm.validators;

import tr.kontas.erp.app.crm.dtos.CreateLeadInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateLeadInputValidator extends Validator<CreateLeadInput> {
    public CreateLeadInputValidator() {
        ruleFor(CreateLeadInput::companyId).notNull().notBlank();
        ruleFor(CreateLeadInput::title).notNull().notBlank();
    }
}
