package tr.kontas.erp.reporting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.reporting.application.saved.CreateSavedReportCommand;
import tr.kontas.erp.reporting.application.saved.CreateSavedReportUseCase;
import tr.kontas.erp.reporting.application.saved.DeleteSavedReportUseCase;
import tr.kontas.erp.reporting.application.saved.GetSavedReportsUseCase;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;
import tr.kontas.erp.reporting.domain.saved.SavedReport;
import tr.kontas.erp.reporting.domain.saved.SavedReportId;
import tr.kontas.erp.reporting.domain.saved.SavedReportRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedReportService implements
        CreateSavedReportUseCase,
        GetSavedReportsUseCase,
        DeleteSavedReportUseCase {

    private final SavedReportRepository repository;

    @Override
    @Transactional
    public SavedReportId execute(CreateSavedReportCommand command) {
        var tenantId = TenantContext.get();
        var id = SavedReportId.newId();
        var saved = new SavedReport(
                id,
                tenantId,
                ReportDefinitionId.of(command.reportDefinitionId()),
                command.name(),
                command.savedFiltersJson(),
                command.savedSortsJson(),
                command.shared(),
                null,
                Instant.now()
        );
        repository.save(saved);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavedReport> execute(String userId) {
        var tenantId = TenantContext.get();
        List<SavedReport> result = new ArrayList<>(repository.findByCreatedBy(userId, tenantId));
        result.addAll(repository.findShared(tenantId));
        return result.stream().distinct().toList();
    }

    @Override
    @Transactional
    public void executeDelete(String savedReportId) {
        repository.deleteById(SavedReportId.of(savedReportId));
    }
}

