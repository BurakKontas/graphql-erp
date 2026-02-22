package tr.kontas.erp.reporting.platform.readmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "rpt_hr_employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RptHrEmployee {

    @Id
    @Column(name = "employee_id")
    private UUID employeeId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "employee_number")
    private String employeeNumber;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "position_title")
    private String positionTitle;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "employment_type")
    private String employmentType;

    private String status;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "country_code")
    private String countryCode;
}

