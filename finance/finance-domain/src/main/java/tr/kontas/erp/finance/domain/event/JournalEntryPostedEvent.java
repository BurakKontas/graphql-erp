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
public class JournalEntryPostedEvent extends DomainEvent {
    private UUID entryId;
    private UUID tenantId;
    private UUID companyId;
    private String entryNumber;
    private String type;
    private String referenceType;
    private String referenceId;
    private LocalDate entryDate;
    private BigDecimal totalAmount;

    @Override
    public UUID getAggregateId() {
        return entryId;
    }

    @Override
    public String getAggregateType() {
        return "JournalEntry";
    }
}

