package tr.kontas.erp.reporting.application.run;

import java.util.List;
import java.util.Map;

public record RunReportResult(
        List<String> columns,
        List<Map<String, Object>> rows,
        long totalCount,
        byte[] exportData,
        String exportFormat
) {}

