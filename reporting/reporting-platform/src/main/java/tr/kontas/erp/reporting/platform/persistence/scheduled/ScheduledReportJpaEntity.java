package tr.kontas.erp.reporting.platform.persistence.scheduled;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "scheduled_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledReportJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "report_definition_id", nullable = false)
    private UUID reportDefinitionId;

    @Column(nullable = false)
    private String name;

    @Column(name = "cron_expression", nullable = false)
    private String cronExpression;

    @Column(nullable = false)
    private String format;

    @Column(name = "recipient_emails", columnDefinition = "TEXT")
    private String recipientEmails;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "last_run_at")
    private Instant lastRunAt;

    @Column(name = "next_run_at")
    private Instant nextRunAt;

    @Column(name = "created_by")
    private String createdBy;
}

