package tr.kontas.erp.reporting.application.scheduled;

import tr.kontas.erp.reporting.domain.scheduled.ScheduledReport;

import java.util.List;

public interface GetScheduledReportsUseCase {
    List<ScheduledReport> execute();
}

