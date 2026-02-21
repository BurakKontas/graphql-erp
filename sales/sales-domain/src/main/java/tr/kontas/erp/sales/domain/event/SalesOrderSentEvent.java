package tr.kontas.erp.sales.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderSentEvent extends DomainEvent {
    private UUID orderId;
    private UUID tenantId;
    private UUID companyId;
    private LocalDate expiryDate;

    public SalesOrderSentEvent(SalesOrderId orderId, TenantId tenantId, CompanyId companyId,
                               LocalDate expiryDate) {
        this.orderId = orderId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.expiryDate = expiryDate;
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
