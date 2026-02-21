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
public class CreditNotePostedEvent extends DomainEvent {
    private UUID creditNoteId;
    private UUID tenantId;
    private UUID companyId;
    private String creditNoteNumber;
    private String invoiceId;
    private String customerId;
    private BigDecimal total;
    private LocalDate creditNoteDate;

    @Override
    public UUID getAggregateId() {
        return creditNoteId;
    }

    @Override
    public String getAggregateType() {
        return "CreditNote";
    }
}

