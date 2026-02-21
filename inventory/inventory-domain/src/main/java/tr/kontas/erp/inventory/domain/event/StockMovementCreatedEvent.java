package tr.kontas.erp.inventory.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.stockmovement.MovementType;
import tr.kontas.erp.inventory.domain.stockmovement.ReferenceType;
import tr.kontas.erp.inventory.domain.stockmovement.StockMovementId;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementCreatedEvent extends DomainEvent {
    private UUID movementId;
    private UUID tenantId;
    private UUID companyId;
    private UUID itemId;
    private UUID warehouseId;
    private String movementType;   // RECEIPT / ISSUE / TRANSFER / ADJUSTMENT
    private BigDecimal quantity;
    private String referenceType;  // SALES_ORDER / PURCHASE_ORDER / MANUAL vb.
    private String referenceId;    // nullable
    private LocalDate movementDate;

    public StockMovementCreatedEvent(StockMovementId movementId, TenantId tenantId, CompanyId companyId,
                                     ItemId itemId, WarehouseId warehouseId,
                                     MovementType movementType, BigDecimal quantity,
                                     ReferenceType referenceType, String referenceId,
                                     LocalDate movementDate) {
        this.movementId = movementId.asUUID();
        this.tenantId = tenantId.asUUID();
        this.companyId = companyId.asUUID();
        this.itemId = itemId.asUUID();
        this.warehouseId = warehouseId.asUUID();
        this.movementType = movementType.name();
        this.quantity = quantity;
        this.referenceType = referenceType.name();
        this.referenceId = referenceId;
        this.movementDate = movementDate;
    }

    @Override
    public UUID getAggregateId() {
        return movementId;
    }

    @Override
    public String getAggregateType() {
        return "StockMovement";
    }
}