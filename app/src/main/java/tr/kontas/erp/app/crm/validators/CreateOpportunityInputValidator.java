package tr.kontas.erp.app.crm.validators;

import tr.kontas.erp.app.crm.dtos.CreateOpportunityInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateOpportunityInputValidator extends Validator<CreateOpportunityInput> {
    public CreateOpportunityInputValidator() {
        ruleFor(CreateOpportunityInput::companyId).notNull().notBlank();
        ruleFor(CreateOpportunityInput::title).notNull().notBlank();
    }
}
