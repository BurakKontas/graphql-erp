package tr.kontas.erp.shipment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.shipment.ShipmentId;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDispatchedEvent extends DomainEvent {
    private UUID shipmentId;
    private UUID tenantId;
    private UUID companyId;
    private String deliveryOrderId;
    private String salesOrderId;
    private String trackingNumber;
    private String carrierName;

    public ShipmentDispatchedEvent(ShipmentId shipmentId, TenantId tenantId,
                                   CompanyId companyId, String deliveryOrderId,
                                   String salesOrderId, String trackingNumber,
                                   String carrierName) {
        this.shipmentId = shipmentId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.deliveryOrderId = deliveryOrderId;
        this.salesOrderId = salesOrderId;
        this.trackingNumber = trackingNumber;
        this.carrierName = carrierName;
    }

    @Override
    public UUID getAggregateId() {
        return shipmentId;
    }

    @Override
    public String getAggregateType() {
        return "Shipment";
    }
}

