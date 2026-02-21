package tr.kontas.erp.sales.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderLineId;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderLineAddedEvent extends DomainEvent {
    private UUID orderId;
    private UUID lineId;
    private UUID tenantId;
    private String itemId;
    private String itemDescription;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String currencyCode;
    private String taxCode;
    private BigDecimal taxRate;

    public SalesOrderLineAddedEvent(SalesOrderId orderId, SalesOrderLineId lineId, TenantId tenantId,
                                    String itemId, String itemDescription, BigDecimal quantity,
                                    BigDecimal unitPrice, Currency currency, Tax tax) {
        this.orderId = orderId.asUUID();
        this.lineId = lineId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.currencyCode = currency != null ? currency.getId().getValue() : null;
        this.taxCode = tax != null ? tax.getId().getValue() : null;
        this.taxRate = tax != null ? tax.getRate().getValue() : null;
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
