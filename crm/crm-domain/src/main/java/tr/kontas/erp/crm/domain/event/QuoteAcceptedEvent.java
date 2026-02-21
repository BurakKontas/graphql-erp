package tr.kontas.erp.crm.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuoteAcceptedEvent extends DomainEvent {

    private UUID quoteId;
    private UUID tenantId;
    private UUID companyId;
    private String quoteNumber;
    private String opportunityId;

    @Override
    public UUID getAggregateId() {
        return quoteId;
    }


    @Override
    public String getAggregateType() {
        return "Quote";
    }
}

