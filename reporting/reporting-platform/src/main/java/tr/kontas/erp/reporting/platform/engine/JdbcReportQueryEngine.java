package tr.kontas.erp.reporting.platform.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.reporting.domain.engine.ReportQueryEngine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class JdbcReportQueryEngine implements ReportQueryEngine {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final Set<String> ALLOWED_TABLES = Set.of(
            "rpt_sales_orders", "rpt_sales_order_lines",
            "rpt_inventory_stock", "rpt_stock_movements",
            "rpt_finance_invoices", "rpt_finance_journals", "rpt_gl_balances",
            "rpt_purchase_orders", "rpt_vendor_invoices",
            "rpt_hr_employees", "rpt_hr_attendance", "rpt_hr_payroll", "rpt_hr_leave_balances",
            "rpt_crm_opportunities", "rpt_crm_leads",
            "rpt_shipments"
    );

    private static final Pattern TABLE_NAME_PATTERN = Pattern.compile("^[a-z_]+$");

    @Override
    public ReportResult execute(String dataSource, Map<String, Object> parameters, int page, int size) {
        validateDataSource(dataSource);

        var params = new MapSqlParameterSource();
        if (parameters != null) {
            parameters.forEach(params::addValue);
        }

        String tenantId;
        try {
            tenantId = TenantContext.get().getValue().toString();
        } catch (Exception e) {
            throw new SecurityException("Tenant context required for report execution");
        }
        params.addValue("tenantId", java.util.UUID.fromString(tenantId));

        String baseSql = "SELECT * FROM " + dataSource + " WHERE tenant_id = :tenantId";

        String countSql = "SELECT COUNT(*) FROM " + dataSource + " WHERE tenant_id = :tenantId";
        Long totalCount = jdbcTemplate.queryForObject(countSql, params, Long.class);

        int safeSize = Math.max(1, Math.min(size, 1000));
        int safePage = Math.max(0, page);
        params.addValue("limit", safeSize);
        params.addValue("offset", (long) safePage * safeSize);

        String pagedSql = baseSql + " LIMIT :limit OFFSET :offset";

        List<Map<String, Object>> rows = new ArrayList<>();
        List<String> columns = new ArrayList<>();

        jdbcTemplate.query(pagedSql, params, rs -> {
            if (columns.isEmpty()) {
                var meta = rs.getMetaData();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    columns.add(meta.getColumnLabel(i));
                }
            }
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 0; i < columns.size(); i++) {
                row.put(columns.get(i), rs.getObject(i + 1));
            }
            rows.add(row);
        });

        return new ReportResult(rows, columns, totalCount != null ? totalCount : 0L);
    }

    private void validateDataSource(String dataSource) {
        if (dataSource == null || dataSource.isBlank()) {
            throw new IllegalArgumentException("Data source is required");
        }
        String normalized = dataSource.toLowerCase().trim();
        if (!TABLE_NAME_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid data source name");
        }
        if (!ALLOWED_TABLES.contains(normalized)) {
            throw new IllegalArgumentException("Data source not allowed: " + dataSource);
        }
    }
}

