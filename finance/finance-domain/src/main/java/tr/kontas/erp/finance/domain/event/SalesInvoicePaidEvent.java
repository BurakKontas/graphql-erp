package tr.kontas.erp.finance.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesInvoicePaidEvent extends DomainEvent {
    private UUID invoiceId;
    private UUID tenantId;
    private UUID companyId;
    private String invoiceNumber;
    private String salesOrderId;
    private BigDecimal total;

    @Override
    public UUID getAggregateId() {
        return invoiceId;
    }

    @Override
    public String getAggregateType() {
        return "SalesInvoice";
    }
}

