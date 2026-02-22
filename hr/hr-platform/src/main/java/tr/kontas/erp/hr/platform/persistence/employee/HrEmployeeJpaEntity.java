package tr.kontas.erp.hr.platform.persistence.employee;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "hr_employees")
@Getter
@Setter
@NoArgsConstructor
public class HrEmployeeJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "employee_number", nullable = false)
    private String employeeNumber;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "gender")
    private String gender;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "personal_email")
    private String personalEmail;

    @Column(name = "work_email")
    private String workEmail;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address_line")
    private String addressLine;

    @Column(name = "city")
    private String city;

    @Column(name = "contact_country_code")
    private String contactCountryCode;

    @Column(name = "position_id")
    private String positionId;

    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "manager_id")
    private String managerId;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Column(name = "employment_type", nullable = false)
    private String employmentType;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "country_code")
    private String countryCode;
}
