package tr.kontas.erp.shipment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderId;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrderCompletedEvent extends DomainEvent {
    private UUID deliveryOrderId;
    private UUID tenantId;
    private UUID companyId;
    private String salesOrderId;

    public DeliveryOrderCompletedEvent(DeliveryOrderId deliveryOrderId, TenantId tenantId,
                                       CompanyId companyId, String salesOrderId) {
        this.deliveryOrderId = deliveryOrderId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.salesOrderId = salesOrderId;
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

