package tr.kontas.erp.core.domain.tenant;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Getter
public class Tenant extends AggregateRoot<TenantId> {
    private final TenantName name;
    private final TenantCode code;
    private boolean active;

    public Tenant(TenantId id, TenantName name, TenantCode code) {
        super(id);
        this.name = name;
        this.code = code;
        this.active = true;

        registerEvent(new TenantCreatedEvent(id));
    }

    public void deactivate() {
        this.active = false;
    }

}
