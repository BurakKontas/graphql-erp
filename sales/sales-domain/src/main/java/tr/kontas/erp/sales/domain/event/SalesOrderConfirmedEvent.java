package tr.kontas.erp.sales.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderConfirmedEvent extends DomainEvent {
    private UUID orderId;
    private UUID tenantId;
    private UUID companyId;
    private String orderNumber;
    private UUID customerId;
    private String currencyCode;
    private String paymentTermCode;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal total;
    private LocalDate orderDate;

    public SalesOrderConfirmedEvent(SalesOrderId orderId, TenantId tenantId, CompanyId companyId,
                                    SalesOrderNumber orderNumber, BusinessPartnerId customerId,
                                    Currency currency, PaymentTerm paymentTerm,
                                    BigDecimal subtotal, BigDecimal taxTotal, BigDecimal total,
                                    LocalDate orderDate) {
        this.orderId = orderId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.orderNumber = orderNumber.getValue();
        this.customerId = customerId != null ? customerId.asUUID() : null;
        this.currencyCode = currency != null ? currency.getId().getValue() : null;
        this.paymentTermCode = paymentTerm != null ? paymentTerm.getId().getValue() : null;
        this.subtotal = subtotal;
        this.taxTotal = taxTotal;
        this.total = total;
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
