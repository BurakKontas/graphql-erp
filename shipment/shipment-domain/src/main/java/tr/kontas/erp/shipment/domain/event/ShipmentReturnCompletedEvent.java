package tr.kontas.erp.shipment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnId;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentReturnCompletedEvent extends DomainEvent {
    private UUID shipmentReturnId;
    private UUID tenantId;
    private UUID companyId;
    private String shipmentId;
    private String salesOrderId;

    public ShipmentReturnCompletedEvent(ShipmentReturnId shipmentReturnId, TenantId tenantId,
                                        CompanyId companyId, String shipmentId,
                                        String salesOrderId) {
        this.shipmentReturnId = shipmentReturnId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.shipmentId = shipmentId;
        this.salesOrderId = salesOrderId;
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

