package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreateAttendanceInputValidator;

@Data
@Validate(validator = CreateAttendanceInputValidator.class)
public class CreateAttendanceInput implements Validatable {
    private String companyId;
    private String employeeId;
    private String date;
    private String source;
    private String checkIn;
    private String checkOut;
    private String status;
    private String deviceId;
    private String notes;
}
