package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreateLeaveRequestInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateLeaveRequestInputValidator extends Validator<CreateLeaveRequestInput> {
    public CreateLeaveRequestInputValidator() {
        ruleFor(CreateLeaveRequestInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateLeaveRequestInput::getEmployeeId).notNull().notBlank();
        ruleFor(CreateLeaveRequestInput::getStartDate).notNull().notBlank();
    }
}
