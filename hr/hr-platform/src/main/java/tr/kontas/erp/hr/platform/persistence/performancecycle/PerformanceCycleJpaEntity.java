package tr.kontas.erp.hr.platform.persistence.performancecycle;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "performance_cycles")
@Getter
@Setter
@NoArgsConstructor
public class PerformanceCycleJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "review_deadline")
    private LocalDate reviewDeadline;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "goals_json", columnDefinition = "TEXT")
    private String goalsJson;
}
