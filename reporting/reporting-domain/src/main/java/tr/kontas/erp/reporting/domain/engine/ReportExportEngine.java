package tr.kontas.erp.reporting.domain.engine;

import tr.kontas.erp.reporting.domain.ReportFormat;

import java.util.List;
import java.util.Map;

public interface ReportExportEngine {
    byte[] export(ReportFormat format, List<String> columns, List<Map<String, Object>> rows, String reportName);
}

