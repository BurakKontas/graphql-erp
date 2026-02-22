package tr.kontas.erp.reporting.application.saved;

import tr.kontas.erp.reporting.domain.saved.SavedReportId;

public interface CreateSavedReportUseCase {
    SavedReportId execute(CreateSavedReportCommand command);
}

