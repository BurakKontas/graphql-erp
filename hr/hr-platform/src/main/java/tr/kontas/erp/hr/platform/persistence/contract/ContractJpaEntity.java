package tr.kontas.erp.hr.platform.persistence.contract;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@NoArgsConstructor
public class ContractJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "contract_number", nullable = false)
    private String contractNumber;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "contract_type", nullable = false)
    private String contractType;

    @Column(name = "gross_salary", nullable = false)
    private BigDecimal grossSalary;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "payroll_config_id")
    private String payrollConfigId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "components_json", columnDefinition = "TEXT")
    private String componentsJson;
}
