package tr.kontas.erp.inventory.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDeactivatedEvent extends DomainEvent {
    private UUID warehouseId;
    private UUID tenantId;

    public WarehouseDeactivatedEvent(WarehouseId warehouseId, TenantId tenantId) {
        this.warehouseId = warehouseId.asUUID();
        this.tenantId = tenantId.asUUID();
    }

    @Override
    public UUID getAggregateId() {
        return warehouseId;
    }

    @Override
    public String getAggregateType() {
        return "Warehouse";
    }
}