package tr.kontas.erp.sales.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderAcceptedEvent extends DomainEvent {
    private UUID orderId;
    private UUID tenantId;
    private UUID companyId;

    public SalesOrderAcceptedEvent(SalesOrderId orderId, TenantId tenantId, CompanyId companyId) {
        this.orderId = orderId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
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
