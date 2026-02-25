package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreateHrEmployeeInputValidator;

@Data
@Validate(validator = CreateHrEmployeeInputValidator.class)
public class CreateHrEmployeeInput implements Validatable {
    private String companyId;
    private String userId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String nationalId;
    private String gender;
    private String nationality;
    private String personalEmail;
    private String workEmail;
    private String phone;
    private String addressLine;
    private String city;
    private String contactCountryCode;
    private String positionId;
    private String departmentId;
    private String managerId;
    private String hireDate;
    private String employmentType;
    private String countryCode;
}
