package tr.kontas.erp.inventory.domain.category;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.event.*;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Category extends AggregateRoot<CategoryId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private CategoryName name;
    private CategoryId parentCategoryId;
    private boolean active;

    public Category(
            CategoryId id,
            TenantId tenantId,
            CompanyId companyId,
            CategoryName name,
            CategoryId parentCategoryId) {
        super(id);

        if (tenantId == null)
            throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null)
            throw new IllegalArgumentException("companyId cannot be null");
        if (name == null)
            throw new IllegalArgumentException("name cannot be null");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.name = name;
        this.parentCategoryId = parentCategoryId;
        this.active = true;

        registerEvent(new CategoryCreatedEvent(id, tenantId, companyId, name));
    }

    public Category(
            CategoryId id,
            TenantId tenantId,
            CompanyId companyId,
            CategoryName name,
            CategoryId parentCategoryId,
            boolean active) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.name = name;
        this.parentCategoryId = parentCategoryId;
        this.active = active;
    }

    public void rename(CategoryName newName) {
        if (newName == null)
            throw new IllegalArgumentException("name cannot be null");
        if (this.name.equals(newName)) return;

        this.name = newName;

        registerEvent(new CategoryRenamedEvent(getId(), tenantId, newName));
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public boolean isRoot() {
        return parentCategoryId == null;
    }
}