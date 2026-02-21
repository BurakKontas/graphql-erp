package tr.kontas.erp.inventory.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.category.CategoryId;
import tr.kontas.erp.inventory.domain.category.CategoryName;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRenamedEvent extends DomainEvent {
    private UUID categoryId;
    private UUID tenantId;
    private String newName;

    public CategoryRenamedEvent(CategoryId categoryId, TenantId tenantId, CategoryName newName) {
        this.categoryId = categoryId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.newName = newName.getValue();
    }

    @Override
    public UUID getAggregateId() {
        return categoryId;
    }

    @Override
    public String getAggregateType() {
        return "Category";
    }
}