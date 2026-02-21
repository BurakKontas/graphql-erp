package tr.kontas.erp.inventory.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.category.CategoryId;
import tr.kontas.erp.inventory.domain.category.CategoryName;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreatedEvent extends DomainEvent {
    private UUID categoryId;
    private UUID tenantId;
    private UUID companyId;
    private String name;

    public CategoryCreatedEvent(CategoryId categoryId, TenantId tenantId, CompanyId companyId, CategoryName name) {
        this.categoryId = categoryId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.name = name.getValue();
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