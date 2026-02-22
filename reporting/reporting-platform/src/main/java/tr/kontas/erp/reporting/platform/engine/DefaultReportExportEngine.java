package tr.kontas.erp.reporting.platform.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tr.kontas.erp.reporting.domain.ReportFormat;
import tr.kontas.erp.reporting.domain.engine.ReportExportEngine;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultReportExportEngine implements ReportExportEngine {

    private final ObjectMapper objectMapper;

    @Override
    public byte[] export(ReportFormat format, List<String> columns, List<Map<String, Object>> rows, String reportName) {
        return switch (format) {
            case JSON -> exportJson(columns, rows);
            case EXCEL -> exportCsv(columns, rows);
            case PDF -> exportText(columns, rows, reportName);
        };
    }

    private byte[] exportJson(List<String> columns, List<Map<String, Object>> rows) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(Map.of(
                    "columns", columns,
                    "rows", rows
            ));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to export JSON", e);
        }
    }

    private byte[] exportCsv(List<String> columns, List<Map<String, Object>> rows) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos);
        writer.println(columns.stream().map(this::sanitizeCsvCell).collect(Collectors.joining(",")));
        for (Map<String, Object> row : rows) {
            String line = columns.stream()
                    .map(col -> row.get(col) != null ? sanitizeCsvCell(row.get(col).toString()) : "")
                    .collect(Collectors.joining(","));
            writer.println(line);
        }
        writer.flush();
        return baos.toByteArray();
    }

    private String sanitizeCsvCell(String value) {
        if (value == null || value.isEmpty()) return "";
        char first = value.charAt(0);
        if (first == '=' || first == '+' || first == '-' || first == '@' || first == '\t' || first == '\r') {
            value = "'" + value;
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private byte[] exportText(List<String> columns, List<Map<String, Object>> rows, String reportName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos);
        writer.println("=== " + reportName + " ===");
        writer.println();
        writer.println(String.join(" | ", columns));
        writer.println("-".repeat(columns.size() * 20));
        for (Map<String, Object> row : rows) {
            List<String> values = columns.stream()
                    .map(col -> row.get(col) != null ? row.get(col).toString() : "")
                    .toList();
            writer.println(String.join(" | ", values));
        }
        writer.flush();
        return baos.toByteArray();
    }
}
