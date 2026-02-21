package tr.kontas.erp.core.domain.businesspartner;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class BusinessPartnerCreatedEvent extends DomainEvent {
    private UUID businessPartnerId;

    public BusinessPartnerCreatedEvent(BusinessPartnerId id) {
        this.businessPartnerId = id.asUUID();
    }

    @Override
    public UUID getAggregateId() {
        return businessPartnerId;
    }

    @Override
    public String getAggregateType() {
        return "BusinessPartner";
    }
}