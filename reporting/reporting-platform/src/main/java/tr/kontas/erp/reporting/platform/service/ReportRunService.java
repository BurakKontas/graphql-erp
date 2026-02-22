package tr.kontas.erp.reporting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.reporting.application.run.RunReportCommand;
import tr.kontas.erp.reporting.application.run.RunReportResult;
import tr.kontas.erp.reporting.application.run.RunReportUseCase;
import tr.kontas.erp.reporting.domain.ReportFormat;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionRepository;
import tr.kontas.erp.reporting.domain.engine.ReportExportEngine;
import tr.kontas.erp.reporting.domain.engine.ReportQueryEngine;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportRunService implements RunReportUseCase {

    private final ReportDefinitionRepository definitionRepository;
    private final ReportQueryEngine queryEngine;
    private final ReportExportEngine exportEngine;

    @Override
    @Transactional(readOnly = true)
    public RunReportResult execute(RunReportCommand command) {
        var tenantId = TenantContext.get();
        var definition = definitionRepository.findById(ReportDefinitionId.of(command.reportDefinitionId()), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Report definition not found: " + command.reportDefinitionId()));

        Map<String, Object> params = command.parameters() != null ? new HashMap<>(command.parameters()) : new HashMap<>();
        params.put("tenantId", tenantId.asUUID().toString());

        int page = Math.max(command.page(), 0);
        int size = command.size() > 0 ? command.size() : 100;

        var result = queryEngine.execute(definition.getDataSource(), params, page, size);

        byte[] exportData = null;
        String exportFormat = null;
        if (command.format() != null && !command.format().isBlank()) {
            ReportFormat format = ReportFormat.valueOf(command.format());
            exportData = exportEngine.export(format, result.columns(), result.rows(), definition.getName());
            exportFormat = format.name();
        }

        return new RunReportResult(result.columns(), result.rows(), result.totalCount(), exportData, exportFormat);
    }
}

