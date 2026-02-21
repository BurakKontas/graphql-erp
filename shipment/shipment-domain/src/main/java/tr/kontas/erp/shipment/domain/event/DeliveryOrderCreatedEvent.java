package tr.kontas.erp.shipment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderId;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderNumber;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrderCreatedEvent extends DomainEvent {
    private UUID deliveryOrderId;
    private UUID tenantId;
    private UUID companyId;
    private String number;
    private String salesOrderId;
    private String salesOrderNumber;
    private String customerId;

    public DeliveryOrderCreatedEvent(DeliveryOrderId deliveryOrderId, TenantId tenantId,
                                     CompanyId companyId, DeliveryOrderNumber number,
                                     String salesOrderId, String salesOrderNumber,
                                     String customerId) {
        this.deliveryOrderId = deliveryOrderId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.number = number.getValue();
        this.salesOrderId = salesOrderId;
        this.salesOrderNumber = salesOrderNumber;
        this.customerId = customerId;
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

