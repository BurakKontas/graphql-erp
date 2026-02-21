package tr.kontas.erp.sales.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderLineId;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderLineUpdatedEvent extends DomainEvent {
    private UUID orderId;
    private UUID lineId;
    private UUID tenantId;
    private BigDecimal newQuantity;
    private BigDecimal newUnitPrice;
    private String currencyCode;

    public SalesOrderLineUpdatedEvent(SalesOrderId orderId, SalesOrderLineId lineId, TenantId tenantId,
                                      BigDecimal newQuantity, BigDecimal newUnitPrice, Currency currency) {
        this.orderId = orderId.asUUID();
        this.lineId = lineId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.newQuantity = newQuantity;
        this.newUnitPrice = newUnitPrice;
        this.currencyCode = currency != null ? currency.getId().getValue() : null;
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
