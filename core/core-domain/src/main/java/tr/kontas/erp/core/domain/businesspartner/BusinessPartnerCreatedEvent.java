package tr.kontas.erp.core.domain.businesspartner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

@Getter
@RequiredArgsConstructor
public class BusinessPartnerCreatedEvent extends DomainEvent {
    private final BusinessPartnerId companyId;
}