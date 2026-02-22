package tr.kontas.erp.reporting.application.run;

import java.util.Map;

public record RunReportCommand(
        String reportDefinitionId,
        Map<String, Object> parameters,
        String format,
        int page,
        int size
) {}

