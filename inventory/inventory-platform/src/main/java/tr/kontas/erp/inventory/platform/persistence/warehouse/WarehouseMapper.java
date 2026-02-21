package tr.kontas.erp.inventory.platform.persistence.warehouse;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.warehouse.*;

public class WarehouseMapper {

    public static WarehouseJpaEntity toEntity(Warehouse domain) {
        WarehouseJpaEntity entity = new WarehouseJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setCode(domain.getCode().getValue());
        entity.setName(domain.getName());
        entity.setActive(domain.isActive());
        return entity;
    }

    public static Warehouse toDomain(WarehouseJpaEntity entity) {
        return new Warehouse(
                WarehouseId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                new WarehouseCode(entity.getCode()),
                entity.getName(),
                entity.isActive()
        );
    }
}
