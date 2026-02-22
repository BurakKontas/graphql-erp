package tr.kontas.erp.reporting.application.definition;

import tr.kontas.erp.reporting.domain.definition.ReportDefinition;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;

public interface GetReportDefinitionByIdUseCase {
    ReportDefinition execute(ReportDefinitionId id);
}

