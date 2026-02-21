package tr.kontas.erp.inventory.platform.persistence.stocklevel;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.stocklevel.*;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

public class StockLevelMapper {

    public static StockLevelJpaEntity toEntity(StockLevel domain) {
        StockLevelJpaEntity entity = new StockLevelJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setItemId(domain.getItemId().asUUID());
        entity.setWarehouseId(domain.getWarehouseId().asUUID());
        entity.setQuantityOnHand(domain.getQuantityOnHand());
        entity.setQuantityReserved(domain.getQuantityReserved());
        entity.setReorderPoint(domain.getReorderPoint());
        entity.setAllowNegativeStock(domain.isAllowNegativeStock());
        return entity;
    }

    public static StockLevel toDomain(StockLevelJpaEntity entity) {
        return new StockLevel(
                StockLevelId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                ItemId.of(entity.getItemId()),
                WarehouseId.of(entity.getWarehouseId()),
                entity.isAllowNegativeStock(),
                entity.getQuantityOnHand(),
                entity.getQuantityReserved(),
                entity.getReorderPoint()
        );
    }
}
