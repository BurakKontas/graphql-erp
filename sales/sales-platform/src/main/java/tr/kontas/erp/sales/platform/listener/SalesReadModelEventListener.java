package tr.kontas.erp.sales.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.sales.domain.event.SalesOrderCancelledEvent;
import tr.kontas.erp.sales.domain.event.SalesOrderConfirmedEvent;
import tr.kontas.erp.sales.domain.event.SalesOrderCreatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class SalesReadModelEventListener {

    private final NamedParameterJdbcTemplate jdbc;

    private static final String UPSERT_SQL = """
            INSERT INTO rpt_sales_orders (order_id, tenant_id, company_id, order_number, order_date,
                customer_id, status, subtotal, tax_total, total, currency_code)
            VALUES (:orderId, :tenantId, :companyId, :orderNumber, :orderDate,
                :customerId, :status, :subtotal, :taxTotal, :total, :currencyCode)
            ON CONFLICT (order_id) DO UPDATE SET
                status = EXCLUDED.status,
                subtotal = EXCLUDED.subtotal,
                tax_total = EXCLUDED.tax_total,
                total = EXCLUDED.total
            """;

    @EventListener
    @Transactional
    public void handle(SalesOrderCreatedEvent event) {
        var params = new MapSqlParameterSource()
                .addValue("orderId", event.getOrderId())
                .addValue("tenantId", event.getTenantId())
                .addValue("companyId", event.getCompanyId())
                .addValue("orderNumber", event.getOrderNumber())
                .addValue("orderDate", event.getOrderDate())
                .addValue("customerId", event.getCustomerId())
                .addValue("status", "DRAFT")
                .addValue("subtotal", null)
                .addValue("taxTotal", null)
                .addValue("total", null)
                .addValue("currencyCode", event.getCurrencyCode());
        jdbc.update(UPSERT_SQL, params);
        log.debug("Read model updated for SalesOrder created: {}", event.getOrderId());
    }

    @EventListener
    @Transactional
    public void handle(SalesOrderConfirmedEvent event) {
        var params = new MapSqlParameterSource()
                .addValue("orderId", event.getOrderId())
                .addValue("tenantId", event.getTenantId())
                .addValue("companyId", event.getCompanyId())
                .addValue("orderNumber", event.getOrderNumber())
                .addValue("orderDate", event.getOrderDate())
                .addValue("customerId", event.getCustomerId())
                .addValue("status", "CONFIRMED")
                .addValue("subtotal", event.getSubtotal())
                .addValue("taxTotal", event.getTaxTotal())
                .addValue("total", event.getTotal())
                .addValue("currencyCode", event.getCurrencyCode());
        jdbc.update(UPSERT_SQL, params);
        log.debug("Read model updated for SalesOrder confirmed: {}", event.getOrderId());
    }

    @EventListener
    @Transactional
    public void handle(SalesOrderCancelledEvent event) {
        jdbc.update(
                "UPDATE rpt_sales_orders SET status = 'CANCELLED' WHERE order_id = :orderId",
                new MapSqlParameterSource("orderId", event.getOrderId())
        );
        log.debug("Read model updated for SalesOrder cancelled: {}", event.getOrderId());
    }
}

