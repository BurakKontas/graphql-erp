package tr.kontas.erp.inventory.domain.stockmovement;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;
import tr.kontas.erp.inventory.domain.event.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
public class StockMovement extends AggregateRoot<StockMovementId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final ItemId itemId;
    private final WarehouseId warehouseId;
    private final MovementType movementType;
    private final BigDecimal quantity;
    private final ReferenceType referenceType;
    private final String referenceId;
    private final String note;
    private final LocalDate movementDate;
    private final Instant createdAt;

    public StockMovement(
            StockMovementId id,
            TenantId tenantId,
            CompanyId companyId,
            ItemId itemId,
            WarehouseId warehouseId,
            MovementType movementType,
            BigDecimal quantity,
            ReferenceType referenceType,
            String referenceId,
            String note,
            LocalDate movementDate) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (itemId == null) throw new IllegalArgumentException("itemId cannot be null");
        if (warehouseId == null) throw new IllegalArgumentException("warehouseId cannot be null");
        if (movementType == null) throw new IllegalArgumentException("movementType cannot be null");
        if (referenceType == null) throw new IllegalArgumentException("referenceType cannot be null");
        if (movementDate == null) throw new IllegalArgumentException("movementDate cannot be null");
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("quantity must be positive");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.itemId = itemId;
        this.warehouseId = warehouseId;
        this.movementType = movementType;
        this.quantity = quantity;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.note = note;
        this.movementDate = movementDate;
        this.createdAt = Instant.now();

        registerEvent(new StockMovementCreatedEvent(id, tenantId, companyId, itemId, warehouseId, movementType, quantity, referenceType, referenceId, movementDate));
    }
}