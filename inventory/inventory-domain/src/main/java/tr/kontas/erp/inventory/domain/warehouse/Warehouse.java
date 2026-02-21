package tr.kontas.erp.inventory.domain.warehouse;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.event.*;

@Getter
public class Warehouse extends AggregateRoot<WarehouseId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final WarehouseCode code; // immutable
    private String name;
    private boolean active;

    public Warehouse(
            WarehouseId id,
            TenantId tenantId,
            CompanyId companyId,
            WarehouseCode code,
            String name) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (code == null) throw new IllegalArgumentException("code cannot be null");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name cannot be blank");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.code = code;
        this.name = name;
        this.active = true;

        registerEvent(new WarehouseCreatedEvent(id, tenantId, companyId, code, name));
    }

    public Warehouse(
            WarehouseId id,
            TenantId tenantId,
            CompanyId companyId,
            WarehouseCode code,
            String name,
            boolean active) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.code = code;
        this.name = name;
        this.active = active;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank())
            throw new IllegalArgumentException("name cannot be blank");
        this.name = newName.trim();
    }

    public void deactivate() {
        this.active = false;
        registerEvent(new WarehouseDeactivatedEvent(getId(), tenantId));
    }

    public void activate() {
        this.active = true;
    }
}