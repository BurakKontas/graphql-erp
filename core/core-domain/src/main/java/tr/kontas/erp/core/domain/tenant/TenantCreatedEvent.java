package tr.kontas.erp.core.domain.tenant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class TenantCreatedEvent extends DomainEvent {
    private UUID tenantId;

    public TenantCreatedEvent(TenantId tenantId) {
        this.tenantId = tenantId.asUUID();
    }

    @Override
    public UUID getAggregateId() {
        return tenantId;
    }

    @Override
    public String getAggregateType() {
        return "Tenant";
    }
}
