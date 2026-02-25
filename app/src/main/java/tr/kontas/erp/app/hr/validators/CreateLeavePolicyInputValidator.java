package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreateLeavePolicyInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateLeavePolicyInputValidator extends Validator<CreateLeavePolicyInput> {
    public CreateLeavePolicyInputValidator() {
        ruleFor(CreateLeavePolicyInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateLeavePolicyInput::getName).notNull().notBlank();
        ruleFor(CreateLeavePolicyInput::getCountryCode).notNull().notBlank();
    }
}
