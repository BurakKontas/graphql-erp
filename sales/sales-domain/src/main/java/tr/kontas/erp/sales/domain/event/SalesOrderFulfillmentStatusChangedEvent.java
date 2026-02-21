package tr.kontas.erp.sales.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.FulfillmentStatus;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderFulfillmentStatusChangedEvent extends DomainEvent {
    private UUID orderId;
    private UUID tenantId;
    private String fulfillmentStatus;

    public SalesOrderFulfillmentStatusChangedEvent(SalesOrderId orderId, TenantId tenantId,
                                                   FulfillmentStatus fulfillmentStatus) {
        this.orderId = orderId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.fulfillmentStatus = fulfillmentStatus.name();
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
