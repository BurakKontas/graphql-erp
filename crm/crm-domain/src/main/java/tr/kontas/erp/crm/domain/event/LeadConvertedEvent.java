package tr.kontas.erp.crm.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeadConvertedEvent extends DomainEvent {

    private UUID leadId;
    private UUID tenantId;
    private UUID companyId;
    private String leadNumber;
    private String opportunityId;

    @Override
    public UUID getAggregateId() {
        return leadId;
    }


    @Override
    public String getAggregateType() {
        return "Lead";
    }
}

