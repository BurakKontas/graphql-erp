package tr.kontas.erp.inventory.platform.persistence.item;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.category.CategoryId;
import tr.kontas.erp.inventory.domain.item.*;

public class ItemMapper {

    public static ItemJpaEntity toEntity(Item domain) {
        ItemJpaEntity entity = new ItemJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setCode(domain.getCode().getValue());
        entity.setName(domain.getName().getValue());
        entity.setType(domain.getType().name());
        entity.setUnitCode(domain.getUnit() != null ? domain.getUnit().getId().getValue() : null);
        entity.setCategoryId(domain.getCategoryId() != null ? domain.getCategoryId().asUUID() : null);
        entity.setAllowNegativeStock(domain.isAllowNegativeStock());
        entity.setActive(domain.isActive());
        return entity;
    }

    public static Item toDomain(ItemJpaEntity entity, Unit unit) {
        return new Item(
                ItemId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                new ItemCode(entity.getCode()),
                new ItemName(entity.getName()),
                ItemType.valueOf(entity.getType()),
                unit,
                entity.getCategoryId() != null ? CategoryId.of(entity.getCategoryId()) : null,
                entity.isAllowNegativeStock(),
                entity.isActive()
        );
    }
}
