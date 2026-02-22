package tr.kontas.erp.reporting.platform.persistence.scheduled;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.reporting.domain.ReportFormat;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;
import tr.kontas.erp.reporting.domain.scheduled.ScheduledReport;
import tr.kontas.erp.reporting.domain.scheduled.ScheduledReportId;

import java.util.Arrays;
import java.util.List;

public class ScheduledReportMapper {

    public static ScheduledReport toDomain(ScheduledReportJpaEntity e) {
        List<String> emails = e.getRecipientEmails() != null
                ? Arrays.asList(e.getRecipientEmails().split(","))
                : List.of();
        return new ScheduledReport(
                ScheduledReportId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                ReportDefinitionId.of(e.getReportDefinitionId()),
                e.getName(),
                e.getCronExpression(),
                ReportFormat.valueOf(e.getFormat()),
                emails,
                e.isActive(),
                e.getLastRunAt(),
                e.getNextRunAt(),
                e.getCreatedBy()
        );
    }

    public static ScheduledReportJpaEntity toJpa(ScheduledReport d) {
        String emails = d.getRecipientEmails() != null ? String.join(",", d.getRecipientEmails()) : null;
        return new ScheduledReportJpaEntity(
                d.getId().asUUID(),
                d.getTenantId().asUUID(),
                d.getReportDefinitionId().asUUID(),
                d.getName(),
                d.getCronExpression(),
                d.getFormat().name(),
                emails,
                d.isActive(),
                d.getLastRunAt(),
                d.getNextRunAt(),
                d.getCreatedBy()
        );
    }
}

