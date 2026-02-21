package tr.kontas.erp.sales.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.domain.reference.tax.TaxRateChangedEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.application.port.TaxResolutionPort;
import tr.kontas.erp.sales.domain.salesorder.SalesOrder;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaxRateChangedEventListener {

    private final TaxResolutionPort taxResolutionPort;
    private final SalesOrderRepository salesOrderRepository;

    @EventListener
    @Transactional
    public void handle(TaxRateChangedEvent event) {
        TenantId tenantId = TenantId.of(event.getTenantId());
        CompanyId companyId = CompanyId.of(event.getCompanyId());

        Tax updatedTax;
        try {
            updatedTax = taxResolutionPort.resolveTax(tenantId, companyId, event.getTaxCode());
        } catch (IllegalArgumentException e) {
            log.warn("TaxRateChangedEvent: tax not found ({}), marking DRAFT orders as NEEDS_ACTION", event.getTaxCode());
            markDraftsAsNeedsAction(tenantId, companyId, event.getTaxCode());
            return;
        }

        List<SalesOrder> drafts = salesOrderRepository.findDraftsByCompanyId(tenantId, companyId);

        int updated = 0;
        for (SalesOrder order : drafts) {
            if (order.updateTaxOnLines(updatedTax)) {
                salesOrderRepository.save(order);
                updated++;
            }
        }

        if (updated > 0) {
            log.info("Tax rate changed for {} → updated {} DRAFT sales order(s)", event.getTaxCode(), updated);
        }
    }

    private void markDraftsAsNeedsAction(TenantId tenantId, CompanyId companyId, String taxCode) {
        List<SalesOrder> drafts = salesOrderRepository.findDraftsByCompanyId(tenantId, companyId);

        int marked = 0;
        for (SalesOrder order : drafts) {
            boolean hasTax = order.getLines().stream()
                    .anyMatch(line -> line.getTax() != null && taxCode.equals(line.getTax().getId().getValue()));
            if (hasTax) {
                order.markNeedsAction();
                salesOrderRepository.save(order);
                marked++;
            }
        }

        if (marked > 0) {
            log.info("Marked {} DRAFT sales order(s) as NEEDS_ACTION due to missing tax: {}", marked, taxCode);
        }
    }
}
