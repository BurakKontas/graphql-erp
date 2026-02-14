package tr.kontas.erp.core.domain.company;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

@Getter
public class CompanyCreatedEvent extends DomainEvent {
    private final CompanyId companyId;

    public CompanyCreatedEvent(CompanyId companyId) {
        this.companyId = companyId;
    }

}