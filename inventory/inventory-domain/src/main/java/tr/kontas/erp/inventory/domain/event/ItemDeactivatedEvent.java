package tr.kontas.erp.inventory.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemId;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDeactivatedEvent extends DomainEvent {
    private UUID itemId;
    private UUID tenantId;

    public ItemDeactivatedEvent(ItemId itemId, TenantId tenantId) {
        this.itemId = itemId.asUUID();
        this.tenantId = tenantId.asUUID();
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