package tr.kontas.erp.shipment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.shipment.ShipmentId;
import tr.kontas.erp.shipment.domain.shipment.ShipmentNumber;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentCreatedEvent extends DomainEvent {
    private UUID shipmentId;
    private UUID tenantId;
    private UUID companyId;
    private String number;
    private String deliveryOrderId;
    private String salesOrderId;
    private String warehouseId;

    public ShipmentCreatedEvent(ShipmentId shipmentId, TenantId tenantId,
                                CompanyId companyId, ShipmentNumber number,
                                String deliveryOrderId, String salesOrderId,
                                String warehouseId) {
        this.shipmentId = shipmentId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.number = number.getValue();
        this.deliveryOrderId = deliveryOrderId;
        this.salesOrderId = salesOrderId;
        this.warehouseId = warehouseId;
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

