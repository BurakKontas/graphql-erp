package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreateHrEmployeeInput {
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
