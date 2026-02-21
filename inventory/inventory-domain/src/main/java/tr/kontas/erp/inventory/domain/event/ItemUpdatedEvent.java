 package tr.kontas.erp.inventory.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.item.ItemName;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdatedEvent extends DomainEvent {
    private UUID itemId;
    private UUID tenantId;
    private String newName;
    private String newUnitCode;

    public ItemUpdatedEvent(ItemId itemId, TenantId tenantId, ItemName newName, Unit newUnit) {
        this.itemId = itemId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.newName = newName.getValue();
        this.newUnitCode = newUnit != null ? newUnit.getId().getValue() : null;
    }

    @Override
    public UUID getAggregateId() {
        return itemId;
    }

    @Override
    public String getAggregateType() {
        return "Item";
    }
}