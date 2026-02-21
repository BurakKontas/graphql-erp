package tr.kontas.erp.crm.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityWonEvent extends DomainEvent {

    private UUID opportunityId;
    private UUID tenantId;
    private UUID companyId;
    private String opportunityNumber;
    private String contactId;
    private String salesOrderId;

    @Override
    public UUID getAggregateId() {
        return opportunityId;
    }


    @Override
    public String getAggregateType() {
        return "Opportunity";
    }
}

