package tr.kontas.erp.sales.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.FulfillmentStatus;
import tr.kontas.erp.sales.domain.salesorder.SalesOrder;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderRepository;
import tr.kontas.erp.shipment.domain.event.DeliveryOrderCompletedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryOrderCompletedEventListener {

    private final SalesOrderRepository salesOrderRepository;

    @EventListener
    @Transactional
    public void handle(DeliveryOrderCompletedEvent event) {
        if (event.getSalesOrderId() == null) {
            return;
        }

        TenantId tenantId = TenantId.of(event.getTenantId());
        SalesOrderId salesOrderId = SalesOrderId.of(event.getSalesOrderId());

        SalesOrder order = salesOrderRepository.findById(salesOrderId, tenantId).orElse(null);
        if (order == null) {
            log.warn("DeliveryOrderCompletedEvent: SalesOrder not found: {}", event.getSalesOrderId());
            return;
        }

        order.updateFulfillmentStatus(FulfillmentStatus.FULFILLED);
        salesOrderRepository.save(order);

        log.info("SalesOrder {} fulfillment status updated to FULFILLED (delivery order {} completed)",
                event.getSalesOrderId(), event.getDeliveryOrderId());
    }
}

