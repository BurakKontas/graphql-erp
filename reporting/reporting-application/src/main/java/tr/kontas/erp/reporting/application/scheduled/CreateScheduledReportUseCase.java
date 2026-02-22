package tr.kontas.erp.reporting.application.scheduled;

import tr.kontas.erp.reporting.domain.scheduled.ScheduledReportId;

public interface CreateScheduledReportUseCase {
    ScheduledReportId execute(CreateScheduledReportCommand command);
}

