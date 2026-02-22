package tr.kontas.erp.reporting.domain.engine;

import java.util.List;
import java.util.Map;

public interface ReportQueryEngine {
    ReportResult execute(String dataSource, Map<String, Object> parameters, int page, int size);

    record ReportResult(List<Map<String, Object>> rows, List<String> columns, long totalCount) {}
}

