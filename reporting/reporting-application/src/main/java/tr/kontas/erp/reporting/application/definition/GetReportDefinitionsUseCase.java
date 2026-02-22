package tr.kontas.erp.reporting.application.definition;

import tr.kontas.erp.reporting.domain.definition.ReportDefinition;

import java.util.List;

public interface GetReportDefinitionsUseCase {
    List<ReportDefinition> execute();
}

