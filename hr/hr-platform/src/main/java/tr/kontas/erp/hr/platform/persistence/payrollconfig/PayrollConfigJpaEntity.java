package tr.kontas.erp.hr.platform.persistence.payrollconfig;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payroll_configs")
@Getter
@Setter
@NoArgsConstructor
public class PayrollConfigJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "valid_year", nullable = false)
    private int validYear;

    @Column(name = "minimum_wage")
    private BigDecimal minimumWage;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "tax_brackets_json", columnDefinition = "TEXT")
    private String taxBracketsJson;

    @Column(name = "deductions_json", columnDefinition = "TEXT")
    private String deductionsJson;

    @Column(name = "active", nullable = false)
    private boolean active;
}
