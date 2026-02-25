package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreateHrEmployeeInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateHrEmployeeInputValidator extends Validator<CreateHrEmployeeInput> {
    public CreateHrEmployeeInputValidator() {
        ruleFor(CreateHrEmployeeInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateHrEmployeeInput::getFirstName).notNull().notBlank();
        ruleFor(CreateHrEmployeeInput::getLastName).notNull().notBlank();
    }
}
