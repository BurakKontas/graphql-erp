package tr.kontas.erp.app.crm.validators;

import tr.kontas.erp.app.crm.dtos.CreateContactInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateContactInputValidator extends Validator<CreateContactInput> {
    public CreateContactInputValidator() {
        ruleFor(CreateContactInput::companyId).notNull().notBlank();
        ruleFor(CreateContactInput::firstName).notNull().notBlank();
        ruleFor(CreateContactInput::lastName).notNull().notBlank();
    }
}
