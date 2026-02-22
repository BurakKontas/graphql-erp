package tr.kontas.erp.reporting.application.definition;

public record UpdateReportDefinitionCommand(
        String definitionId,
        String name,
        String description,
        String columnsJson,
        String filtersJson,
        String sqlQuery
) {}

