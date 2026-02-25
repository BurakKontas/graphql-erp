package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreateContractInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateContractInputValidator extends Validator<CreateContractInput> {
    public CreateContractInputValidator() {
        ruleFor(CreateContractInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateContractInput::getEmployeeId).notNull().notBlank();
        ruleFor(CreateContractInput::getStartDate).notNull().notBlank();
    }
}
