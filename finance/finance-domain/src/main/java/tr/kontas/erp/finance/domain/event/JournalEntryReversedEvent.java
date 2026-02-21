package tr.kontas.erp.finance.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryReversedEvent extends DomainEvent {
    private UUID entryId;
    private UUID tenantId;
    private UUID companyId;
    private String entryNumber;
    private String referenceType;
    private String referenceId;

    @Override
    public UUID getAggregateId() {
        return entryId;
    }

    @Override
    public String getAggregateType() {
        return "JournalEntry";
    }
}

