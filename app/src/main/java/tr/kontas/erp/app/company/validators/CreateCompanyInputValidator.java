package tr.kontas.erp.app.company.validators;

import tr.kontas.fluentvalidation.validation.Validator;
import tr.kontas.erp.app.company.dtos.CreateCompanyInput;

public class CreateCompanyInputValidator extends Validator<CreateCompanyInput> {
    public CreateCompanyInputValidator() {
        ruleFor(CreateCompanyInput::getName)
                .notNull()
                .notBlank()
                .withMessage("Company name is required");
    }
}
