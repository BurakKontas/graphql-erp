package tr.kontas.erp.inventory.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.stocklevel.StockLevelId;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockReservationReleasedEvent extends DomainEvent {
    private UUID stockLevelId;
    private UUID tenantId;
    private UUID companyId;
    private UUID itemId;
    private UUID warehouseId;
    private BigDecimal releasedQuantity;
    private BigDecimal totalReserved;
    private BigDecimal availableAfter;
    private String referenceId; // SalesOrderId

    public StockReservationReleasedEvent(StockLevelId stockLevelId, TenantId tenantId, CompanyId companyId,
                                         ItemId itemId, WarehouseId warehouseId,
                                         BigDecimal releasedQuantity, BigDecimal totalReserved,
                                         BigDecimal availableAfter, String referenceId) {
        this.stockLevelId = stockLevelId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.itemId = itemId.asUUID();
        this.warehouseId = warehouseId.asUUID();
        this.releasedQuantity = releasedQuantity;
        this.totalReserved = totalReserved;
        this.availableAfter = availableAfter;
        this.referenceId = referenceId;
    }

    @Override
    public UUID getAggregateId() {
        return stockLevelId;
    }

    @Override
    public String getAggregateType() {
        return "StockLevel";
    }
}