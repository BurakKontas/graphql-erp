package tr.kontas.erp.reporting.domain.scheduled;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.reporting.domain.ReportFormat;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class ScheduledReport {

    private ScheduledReportId id;
    private TenantId tenantId;
    private ReportDefinitionId reportDefinitionId;
    private String name;
    private String cronExpression;
    private ReportFormat format;
    private List<String> recipientEmails;
    private boolean active;
    private Instant lastRunAt;
    private Instant nextRunAt;
    private String createdBy;

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void recordRun(Instant runAt, Instant nextRun) {
        this.lastRunAt = runAt;
        this.nextRunAt = nextRun;
    }
}

