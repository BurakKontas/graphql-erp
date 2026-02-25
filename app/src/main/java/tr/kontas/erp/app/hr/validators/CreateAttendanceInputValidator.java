package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreateAttendanceInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateAttendanceInputValidator extends Validator<CreateAttendanceInput> {
    public CreateAttendanceInputValidator() {
        ruleFor(CreateAttendanceInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateAttendanceInput::getEmployeeId).notNull().notBlank();
        ruleFor(CreateAttendanceInput::getDate).notNull().notBlank();
    }
}
