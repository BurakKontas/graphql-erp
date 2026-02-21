package tr.kontas.erp.finance.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.event.PaymentPostedEvent;
import tr.kontas.erp.finance.domain.salesinvoice.SalesInvoiceId;
import tr.kontas.erp.finance.domain.salesinvoice.SalesInvoiceRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentPostedEventListener {

    private final SalesInvoiceRepository salesInvoiceRepository;

    @EventListener
    @Transactional
    public void handle(PaymentPostedEvent event) {
        if (event.getInvoiceId() == null) {
            log.debug("PaymentPostedEvent: no invoiceId, skipping invoice update");
            return;
        }

        TenantId tenantId = TenantId.of(event.getTenantId());
        SalesInvoiceId invoiceId = SalesInvoiceId.of(event.getInvoiceId());

        salesInvoiceRepository.findById(invoiceId, tenantId).ifPresent(invoice -> {
            invoice.recordPayment(event.getAmount());
            salesInvoiceRepository.save(invoice);
            log.info("Updated invoice {} with payment of {}", event.getInvoiceId(), event.getAmount());
        });
    }
}

