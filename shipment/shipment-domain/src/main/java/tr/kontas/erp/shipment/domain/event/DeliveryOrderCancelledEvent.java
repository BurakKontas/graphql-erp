package tr.kontas.erp.shipment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.deliveryorder.CancellationReason;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderId;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderStatus;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrderCancelledEvent extends DomainEvent {
    private UUID deliveryOrderId;
    private UUID tenantId;
    private UUID companyId;
    private String salesOrderId;
    private String reason;
    private String statusAtCancellation;

    public DeliveryOrderCancelledEvent(DeliveryOrderId deliveryOrderId, TenantId tenantId,
                                       CompanyId companyId, String salesOrderId,
                                       CancellationReason reason,
                                       DeliveryOrderStatus statusAtCancellation) {
        this.deliveryOrderId = deliveryOrderId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.salesOrderId = salesOrderId;
        this.reason = reason.getValue();
        this.statusAtCancellation = statusAtCancellation.name();
    }

    @Override
    public UUID getAggregateId() {
        return deliveryOrderId;
    }

    @Override
    public String getAggregateType() {
        return "DeliveryOrder";
    }
}

