package tr.kontas.erp.sales.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.CancellationReason;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderStatus;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderCancelledEvent extends DomainEvent {
    private UUID orderId;
    private UUID tenantId;
    private UUID companyId;
    private String reason;
    private String statusAtCancellation;

    public SalesOrderCancelledEvent(SalesOrderId orderId, TenantId tenantId, CompanyId companyId,
                                    CancellationReason reason, SalesOrderStatus statusAtCancellation) {
        this.orderId = orderId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.reason = reason.getValue();
        this.statusAtCancellation = statusAtCancellation.name();
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
