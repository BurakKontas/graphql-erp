package tr.kontas.erp.finance.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.finance.domain.event.SalesInvoicePaidEvent;
import tr.kontas.erp.finance.domain.event.SalesInvoicePostedEvent;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceReadModelEventListener {

    private final NamedParameterJdbcTemplate jdbc;

    @EventListener
    @Transactional
    public void handle(SalesInvoicePostedEvent event) {
        var params = new MapSqlParameterSource()
                .addValue("invoiceId", event.getInvoiceId())
                .addValue("tenantId", event.getTenantId())
                .addValue("companyId", event.getCompanyId())
                .addValue("invoiceNumber", event.getInvoiceNumber())
                .addValue("invoiceDate", event.getInvoiceDate())
                .addValue("customerId", event.getCustomerId())
                .addValue("total", event.getTotal())
                .addValue("paidAmount", BigDecimal.ZERO)
                .addValue("remainingAmount", event.getTotal())
                .addValue("paymentStatus", "NOT_PAID");

        jdbc.update("""
                INSERT INTO rpt_finance_invoices (invoice_id, tenant_id, company_id, invoice_number,
                    invoice_date, customer_id, total, paid_amount, remaining_amount, payment_status)
                VALUES (:invoiceId, :tenantId, :companyId, :invoiceNumber,
                    :invoiceDate, :customerId, :total, :paidAmount, :remainingAmount, :paymentStatus)
                ON CONFLICT (invoice_id) DO UPDATE SET
                    payment_status = EXCLUDED.payment_status,
                    paid_amount = EXCLUDED.paid_amount,
                    remaining_amount = EXCLUDED.remaining_amount
                """, params);
        log.debug("Read model updated for SalesInvoice posted: {}", event.getInvoiceId());
    }

    @EventListener
    @Transactional
    public void handle(SalesInvoicePaidEvent event) {
        jdbc.update("""
                UPDATE rpt_finance_invoices SET
                    paid_amount = total,
                    remaining_amount = 0,
                    payment_status = 'PAID'
                WHERE invoice_id = :invoiceId
                """, new MapSqlParameterSource("invoiceId", event.getInvoiceId()));
        log.debug("Read model updated for SalesInvoice paid: {}", event.getInvoiceId());
    }
}

