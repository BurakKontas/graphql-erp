package tr.kontas.erp.inventory.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemCode;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.item.ItemName;
import tr.kontas.erp.inventory.domain.item.ItemType;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreatedEvent extends DomainEvent {
    private UUID itemId;
    private UUID tenantId;
    private UUID companyId;
    private String code;
    private String name;
    private String type;     // PHYSICAL / SERVICE
    private String unitCode;

    public ItemCreatedEvent(ItemId itemId, TenantId tenantId, CompanyId companyId,
                            ItemCode code, ItemName name, ItemType type, Unit unit) {
        this.itemId = itemId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.code = code.getValue();
        this.name = name.getValue();
        this.type = type.name();
        this.unitCode = unit != null ? unit.getId().getValue() : null;
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