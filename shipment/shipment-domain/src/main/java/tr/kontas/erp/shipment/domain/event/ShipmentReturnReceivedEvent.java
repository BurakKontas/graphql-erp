package tr.kontas.erp.shipment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnId;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentReturnReceivedEvent extends DomainEvent {
    private UUID shipmentReturnId;
    private UUID tenantId;
    private UUID companyId;
    private String shipmentId;
    private String salesOrderId;
    private String warehouseId;
    private List<ReturnLineData> lines;

    public ShipmentReturnReceivedEvent(ShipmentReturnId shipmentReturnId, TenantId tenantId,
                                       CompanyId companyId, String shipmentId,
                                       String salesOrderId, String warehouseId,
                                       List<ReturnLineData> lines) {
        this.shipmentReturnId = shipmentReturnId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.shipmentId = shipmentId;
        this.salesOrderId = salesOrderId;
        this.warehouseId = warehouseId;
        this.lines = lines;
    }

    @Override
    public UUID getAggregateId() {
        return shipmentReturnId;
    }

    @Override
    public String getAggregateType() {
        return "ShipmentReturn";
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnLineData {
        private String itemId;
        private BigDecimal quantity;
        private String unitCode;
    }
}

