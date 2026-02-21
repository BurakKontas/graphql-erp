package tr.kontas.erp.inventory.platform.persistence.category;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.category.*;

public class CategoryMapper {

    public static CategoryJpaEntity toEntity(Category domain) {
        CategoryJpaEntity entity = new CategoryJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setName(domain.getName().getValue());
        entity.setParentCategoryId(domain.getParentCategoryId() != null ? domain.getParentCategoryId().asUUID() : null);
        entity.setActive(domain.isActive());
        return entity;
    }

    public static Category toDomain(CategoryJpaEntity entity) {
        return new Category(
                CategoryId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                new CategoryName(entity.getName()),
                entity.getParentCategoryId() != null ? CategoryId.of(entity.getParentCategoryId()) : null,
                entity.isActive()
        );
    }
}
