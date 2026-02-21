package tr.kontas.erp.shipment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnId;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnNumber;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentReturnCreatedEvent extends DomainEvent {
    private UUID shipmentReturnId;
    private UUID tenantId;
    private UUID companyId;
    private String number;
    private String shipmentId;
    private String salesOrderId;
    private String warehouseId;

    public ShipmentReturnCreatedEvent(ShipmentReturnId shipmentReturnId, TenantId tenantId,
                                      CompanyId companyId, ShipmentReturnNumber number,
                                      String shipmentId, String salesOrderId,
                                      String warehouseId) {
        this.shipmentReturnId = shipmentReturnId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.number = number.getValue();
        this.shipmentId = shipmentId;
        this.salesOrderId = salesOrderId;
        this.warehouseId = warehouseId;
    }

    @Override
    public UUID getAggregateId() {
        return shipmentReturnId;
    }

    @Override
    public String getAggregateType() {
        return "ShipmentReturn";
    }
}

