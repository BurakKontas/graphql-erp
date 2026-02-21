package tr.kontas.erp.inventory.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseCode;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseCreatedEvent extends DomainEvent {
    private UUID warehouseId;
    private UUID tenantId;
    private UUID companyId;
    private String code;
    private String name;

    public WarehouseCreatedEvent(WarehouseId warehouseId, TenantId tenantId,
                                 CompanyId companyId, WarehouseCode code, String name) {
        this.warehouseId = warehouseId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.code = code.getValue();
        this.name = name;
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