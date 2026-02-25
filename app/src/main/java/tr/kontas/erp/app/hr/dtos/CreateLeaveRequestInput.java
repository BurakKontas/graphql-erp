package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreateLeaveRequestInputValidator;

@Data
@Validate(validator = CreateLeaveRequestInputValidator.class)
public class CreateLeaveRequestInput implements Validatable {
    private String companyId;
    private String employeeId;
    private String leaveType;
    private String startDate;
    private String endDate;
    private int requestedDays;
    private String reason;
    private String documentRef;
}
