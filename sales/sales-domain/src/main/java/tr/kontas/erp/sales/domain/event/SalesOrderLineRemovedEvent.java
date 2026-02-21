package tr.kontas.erp.sales.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderLineId;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderLineRemovedEvent extends DomainEvent {
    private UUID orderId;
    private UUID lineId;
    private UUID tenantId;

    public SalesOrderLineRemovedEvent(SalesOrderId orderId, SalesOrderLineId lineId, TenantId tenantId) {
        this.orderId = orderId.asUUID();
        this.lineId = lineId.asUUID();
        this.tenantId = tenantId.asUUID();
    }

    @Override
    public UUID getAggregateId() {
        return orderId;
    }

    @Override
    public String getAggregateType() {
        return "SalesOrder";
    }
}
