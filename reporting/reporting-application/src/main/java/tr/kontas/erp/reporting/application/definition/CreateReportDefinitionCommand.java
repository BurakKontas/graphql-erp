package tr.kontas.erp.reporting.application.definition;

import java.util.List;

public record CreateReportDefinitionCommand(
        String name,
        String description,
        String module,
        String type,
        String dataSource,
        String columnsJson,
        String filtersJson,
        String sqlQuery,
        String requiredPermission,
        boolean systemReport
) {}

