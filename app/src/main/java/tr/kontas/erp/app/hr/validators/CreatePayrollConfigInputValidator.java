package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreatePayrollConfigInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreatePayrollConfigInputValidator extends Validator<CreatePayrollConfigInput> {
    public CreatePayrollConfigInputValidator() {
        ruleFor(CreatePayrollConfigInput::getCompanyId).notNull().notBlank();
        ruleFor(CreatePayrollConfigInput::getName).notNull().notBlank();
        ruleFor(CreatePayrollConfigInput::getCountryCode).notNull().notBlank();
    }
}
