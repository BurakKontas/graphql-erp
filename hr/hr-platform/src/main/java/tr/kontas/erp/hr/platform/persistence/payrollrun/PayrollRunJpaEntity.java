package tr.kontas.erp.hr.platform.persistence.payrollrun;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payroll_runs")
@Getter
@Setter
@NoArgsConstructor
public class PayrollRunJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "run_number", nullable = false)
    private String runNumber;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payroll_config_id")
    private String payrollConfigId;

    @Column(name = "entries_json", columnDefinition = "TEXT")
    private String entriesJson;
}
