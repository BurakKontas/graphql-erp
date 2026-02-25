package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreateLeaveBalanceInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateLeaveBalanceInputValidator extends Validator<CreateLeaveBalanceInput> {
    public CreateLeaveBalanceInputValidator() {
        ruleFor(CreateLeaveBalanceInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateLeaveBalanceInput::getEmployeeId).notNull().notBlank();
        ruleFor(CreateLeaveBalanceInput::getLeaveType).notNull().notBlank();
        ruleFor(CreateLeaveBalanceInput::getYear).greaterThan(1900);
        ruleFor(CreateLeaveBalanceInput::getEntitlementDays).greaterThanOrEqualTo(0);
        ruleFor(CreateLeaveBalanceInput::getCarryoverDays).greaterThanOrEqualTo(0);
    }
}
