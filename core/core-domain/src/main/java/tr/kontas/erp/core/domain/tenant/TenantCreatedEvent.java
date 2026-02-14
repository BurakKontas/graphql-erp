package tr.kontas.erp.core.domain.tenant;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Getter
public class TenantCreatedEvent extends DomainEvent {
    private final TenantId tenantId;

    public TenantCreatedEvent(TenantId tenantId) {
        this.tenantId = tenantId;
    }

}
