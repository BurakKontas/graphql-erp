package tr.kontas.erp.hr.application.employee;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.time.LocalDate;

public record CreateEmployeeCommand(CompanyId companyId, String userId, String firstName, String lastName, LocalDate dateOfBirth, String nationalId, String gender, String nationality, String personalEmail, String workEmail, String phone, String addressLine, String city, String contactCountryCode, String positionId, String departmentId, String managerId, LocalDate hireDate, String employmentType, String countryCode) {}
