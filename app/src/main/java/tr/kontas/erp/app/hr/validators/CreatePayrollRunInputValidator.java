package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreatePayrollRunInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreatePayrollRunInputValidator extends Validator<CreatePayrollRunInput> {
    public CreatePayrollRunInputValidator() {
        ruleFor(CreatePayrollRunInput::getCompanyId).notNull().notBlank();
        ruleFor(CreatePayrollRunInput::getYear).greaterThanOrEqualTo(1900);
        ruleFor(CreatePayrollRunInput::getMonth).greaterThanOrEqualTo(1).lessThanOrEqualTo(12);
    }
}
