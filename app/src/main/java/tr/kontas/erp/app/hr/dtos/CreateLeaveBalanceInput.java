package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.erp.app.hr.validators.CreateLeaveBalanceInputValidator;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;

@Data
@Validate(validator = CreateLeaveBalanceInputValidator.class)
public class CreateLeaveBalanceInput implements Validatable {
    private String companyId;
    private String employeeId;
    private String leaveType;
    private int year;
    private int entitlementDays;
    private int carryoverDays;
}
