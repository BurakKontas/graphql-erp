package tr.kontas.erp.inventory.platform.persistence.stockmovement;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.stockmovement.*;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

public class StockMovementMapper {

    public static StockMovementJpaEntity toEntity(StockMovement domain) {
        StockMovementJpaEntity entity = new StockMovementJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setItemId(domain.getItemId().asUUID());
        entity.setWarehouseId(domain.getWarehouseId().asUUID());
        entity.setMovementType(domain.getMovementType().name());
        entity.setQuantity(domain.getQuantity());
        entity.setReferenceType(domain.getReferenceType().name());
        entity.setReferenceId(domain.getReferenceId());
        entity.setNote(domain.getNote());
        entity.setMovementDate(domain.getMovementDate());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    public static StockMovement toDomain(StockMovementJpaEntity entity) {
        return new StockMovement(
                StockMovementId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                ItemId.of(entity.getItemId()),
                WarehouseId.of(entity.getWarehouseId()),
                MovementType.valueOf(entity.getMovementType()),
                entity.getQuantity(),
                ReferenceType.valueOf(entity.getReferenceType()),
                entity.getReferenceId(),
                entity.getNote(),
                entity.getMovementDate()
        );
    }
}
