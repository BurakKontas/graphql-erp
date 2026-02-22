package tr.kontas.erp.reporting.application.definition;

import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;

public interface CreateReportDefinitionUseCase {
    ReportDefinitionId execute(CreateReportDefinitionCommand command);
}

