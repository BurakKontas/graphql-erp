package tr.kontas.erp.reporting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.reporting.application.scheduled.CreateScheduledReportCommand;
import tr.kontas.erp.reporting.application.scheduled.CreateScheduledReportUseCase;
import tr.kontas.erp.reporting.application.scheduled.DeleteScheduledReportUseCase;
import tr.kontas.erp.reporting.application.scheduled.GetScheduledReportsUseCase;
import tr.kontas.erp.reporting.domain.ReportFormat;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;
import tr.kontas.erp.reporting.domain.scheduled.ScheduledReport;
import tr.kontas.erp.reporting.domain.scheduled.ScheduledReportId;
import tr.kontas.erp.reporting.domain.scheduled.ScheduledReportRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledReportService implements
        CreateScheduledReportUseCase,
        GetScheduledReportsUseCase,
        DeleteScheduledReportUseCase {

    private final ScheduledReportRepository repository;

    @Override
    @Transactional
    public ScheduledReportId execute(CreateScheduledReportCommand command) {
        var tenantId = TenantContext.get();
        var id = ScheduledReportId.newId();
        var scheduled = new ScheduledReport(
                id,
                tenantId,
                ReportDefinitionId.of(command.reportDefinitionId()),
                command.name(),
                command.cronExpression(),
                ReportFormat.valueOf(command.format()),
                command.recipientEmails(),
                true,
                null,
                null,
                null
        );
        repository.save(scheduled);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduledReport> execute() {
        var tenantId = TenantContext.get();
        return repository.findByTenantId(tenantId);
    }

    @Override
    @Transactional
    public void execute(String scheduledReportId) {
        repository.deleteById(ScheduledReportId.of(scheduledReportId));
    }
}

