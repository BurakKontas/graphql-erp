package tr.kontas.erp.finance.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPostedEvent extends DomainEvent {
    private UUID paymentId;
    private UUID tenantId;
    private UUID companyId;
    private String paymentNumber;
    private String paymentType;
    private String invoiceId;
    private String customerId;
    private BigDecimal amount;
    private String currencyCode;
    private LocalDate paymentDate;

    @Override
    public UUID getAggregateId() {
        return paymentId;
    }

    @Override
    public String getAggregateType() {
        return "Payment";
    }
}

