package tr.kontas.erp.sales.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.InvoicingStatus;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderInvoiceRecordedEvent extends DomainEvent {
    private UUID orderId;
    private UUID tenantId;
    private BigDecimal invoicedAmount;
    private BigDecimal totalInvoicedSoFar;
    private String invoicingStatus;

    public SalesOrderInvoiceRecordedEvent(SalesOrderId orderId, TenantId tenantId,
                                          BigDecimal invoicedAmount, BigDecimal totalInvoicedSoFar,
                                          InvoicingStatus invoicingStatus) {
        this.orderId = orderId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.invoicedAmount = invoicedAmount;
        this.totalInvoicedSoFar = totalInvoicedSoFar;
        this.invoicingStatus = invoicingStatus.name();
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
