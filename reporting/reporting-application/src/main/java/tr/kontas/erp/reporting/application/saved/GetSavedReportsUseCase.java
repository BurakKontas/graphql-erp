package tr.kontas.erp.reporting.application.saved;

import tr.kontas.erp.reporting.domain.saved.SavedReport;

import java.util.List;

public interface GetSavedReportsUseCase {
    List<SavedReport> execute(String userId);
}

