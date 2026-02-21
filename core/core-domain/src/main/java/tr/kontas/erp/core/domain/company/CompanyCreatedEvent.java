package tr.kontas.erp.core.domain.company;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyCreatedEvent extends DomainEvent {
    private UUID companyId;

    public CompanyCreatedEvent(CompanyId companyId) {
        this.companyId = companyId.asUUID();
    }

    @Override
    public UUID getAggregateId() {
        return companyId;
    }

    @Override
    public String getAggregateType() {
        return "Company";
    }
}