package tr.kontas.erp.core.domain.company;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Getter
public class Company extends AggregateRoot<CompanyId> {
    private final TenantId tenantId;
    private final CompanyName name;
    private boolean active;

    public Company(CompanyId id, TenantId tenantId, CompanyName name) {
        super(id);

        if (tenantId == null) {
            throw new IllegalArgumentException("TenantId cannot be null");
        }

        this.tenantId = tenantId;
        this.name = name;
        this.active = true;

        registerEvent(new CompanyCreatedEvent(id));
    }

    protected Company(CompanyId id, TenantId tenantId, CompanyName name, boolean active) {
        super(id);
        this.tenantId = tenantId;
        this.name = name;
        this.active = active;
    }

    public void deactivate() {
        this.active = false;
    }
}
