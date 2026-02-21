package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HrEmployeePayload {
    private String id;
    private String companyId;
    private String employeeNumber;
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
    private String terminationDate;
    private String employmentType;
    private String status;
    private String countryCode;
}
