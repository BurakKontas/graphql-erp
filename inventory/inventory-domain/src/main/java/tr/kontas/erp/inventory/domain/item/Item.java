package tr.kontas.erp.inventory.domain.item;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.category.CategoryId;
import tr.kontas.erp.inventory.domain.event.*;

@Getter
public class Item extends AggregateRoot<ItemId> {

    private final TenantId tenantId;
    private final CompanyId companyId;

    private final ItemCode code; // immutable
    private final ItemType type; // immutable

    private ItemName name;
    private Unit unit;
    private CategoryId categoryId;
    private boolean allowNegativeStock;
    private boolean active;

    public Item(
            ItemId id,
            TenantId tenantId,
            CompanyId companyId,
            ItemCode code,
            ItemName name,
            ItemType type,
            Unit unit,
            CategoryId categoryId,
            boolean allowNegativeStock) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (code == null) throw new IllegalArgumentException("code cannot be null");
        if (name == null) throw new IllegalArgumentException("name cannot be null");
        if (type == null) throw new IllegalArgumentException("type cannot be null");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.code = code;
        this.name = name;
        this.type = type;
        this.unit = unit;
        this.categoryId = categoryId;
        this.allowNegativeStock = type.isStockTracked() && allowNegativeStock;
        this.active = true;

        registerEvent(new ItemCreatedEvent(id, tenantId, companyId, code, name, type, unit));
    }

    public Item(
            ItemId id,
            TenantId tenantId,
            CompanyId companyId,
            ItemCode code,
            ItemName name,
            ItemType type,
            Unit unit,
            CategoryId categoryId,
            boolean allowNegativeStock,
            boolean active) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.code = code;
        this.name = name;
        this.type = type;
        this.unit = unit;
        this.categoryId = categoryId;
        this.allowNegativeStock = allowNegativeStock;
        this.active = active;
    }

    public void update(ItemName newName, Unit newUnit, CategoryId newCategoryId) {
        if (newName == null)
            throw new IllegalArgumentException("name cannot be null");

        this.name       = newName;
        this.unit   = newUnit;
        this.categoryId = newCategoryId;

        registerEvent(new ItemUpdatedEvent(getId(), tenantId, newName, newUnit));
    }

    public void allowNegativeStock(boolean allow) {
        if (!type.isStockTracked()) return;
        this.allowNegativeStock = allow;
    }

    public void deactivate() {
        this.active = false;
        registerEvent(new ItemDeactivatedEvent(getId(), tenantId));
    }

    public void activate() {
        this.active = true;
    }
}

