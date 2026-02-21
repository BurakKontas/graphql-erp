package tr.kontas.erp.crm.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContactCreatedEvent extends DomainEvent {

    private UUID contactId;
    private UUID tenantId;
    private UUID companyId;
    private String contactNumber;
    private String contactType;

    @Override
    public UUID getAggregateId() {
        return contactId;
    }


    @Override
    public String getAggregateType() {
        return "Contact";
    }
}

