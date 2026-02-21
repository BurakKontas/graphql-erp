package tr.kontas.erp.sales.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderNumber;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderCreatedEvent extends DomainEvent {
    private UUID orderId;
    private UUID tenantId;
    private UUID companyId;
    private String orderNumber;
    private UUID customerId;
    private String currencyCode;
    private LocalDate orderDate;

    public SalesOrderCreatedEvent(SalesOrderId orderId, TenantId tenantId, CompanyId companyId,
                                  SalesOrderNumber orderNumber, BusinessPartnerId customerId,
                                  Currency currency, LocalDate orderDate) {
        this.orderId = orderId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.orderNumber = orderNumber.getValue();
        this.customerId = customerId != null ? customerId.asUUID() : null;
        this.currencyCode = currency != null ? currency.getId().getValue() : null;
        this.orderDate = orderDate;
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


