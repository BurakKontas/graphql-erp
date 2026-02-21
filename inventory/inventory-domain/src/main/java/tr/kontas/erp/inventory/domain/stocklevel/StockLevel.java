package tr.kontas.erp.inventory.domain.stocklevel;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.exception.StockInsufficientException;
import tr.kontas.erp.inventory.domain.exception.StockOperationNotAllowedException;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;
import tr.kontas.erp.inventory.domain.event.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class StockLevel extends AggregateRoot<StockLevelId> {

    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final ItemId itemId;
    private final WarehouseId warehouseId;

    private BigDecimal quantityOnHand;
    private BigDecimal quantityReserved;
    private BigDecimal reorderPoint; // nullable

    private final boolean allowNegativeStock;

    public StockLevel(
            StockLevelId id,
            TenantId tenantId,
            CompanyId companyId,
            ItemId itemId,
            WarehouseId warehouseId,
            boolean allowNegativeStock) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (itemId == null) throw new IllegalArgumentException("itemId cannot be null");
        if (warehouseId == null) throw new IllegalArgumentException("warehouseId cannot be null");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.itemId = itemId;
        this.warehouseId = warehouseId;
        this.allowNegativeStock = allowNegativeStock;
        this.quantityOnHand = BigDecimal.ZERO;
        this.quantityReserved = BigDecimal.ZERO;
        this.reorderPoint = null;
    }

    public StockLevel(
            StockLevelId id,
            TenantId tenantId,
            CompanyId companyId,
            ItemId itemId,
            WarehouseId warehouseId,
            boolean allowNegativeStock,
            BigDecimal quantityOnHand,
            BigDecimal quantityReserved,
            BigDecimal reorderPoint) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.itemId = itemId;
        this.warehouseId = warehouseId;
        this.allowNegativeStock = allowNegativeStock;
        this.quantityOnHand = scale(quantityOnHand != null ? quantityOnHand : BigDecimal.ZERO);
        this.quantityReserved = scale(quantityReserved != null ? quantityReserved : BigDecimal.ZERO);
        this.reorderPoint = reorderPoint;
    }

    public void reserve(BigDecimal quantity, String referenceId) {
        validatePositive(quantity, "reserve quantity");

        if (!allowNegativeStock) {
            BigDecimal available = getQuantityAvailable();
            if (quantity.compareTo(available) > 0) {
                throw new StockInsufficientException(itemId, warehouseId, quantity, available);
            }
        }

        this.quantityReserved = scale(this.quantityReserved.add(quantity));

        registerEvent(new StockReservedEvent(getId(), tenantId, companyId, itemId, warehouseId, quantity, this.quantityReserved, getQuantityAvailable(), referenceId));
    }

    public void releaseReservation(BigDecimal quantity, String referenceId) {
        validatePositive(quantity, "release quantity");

        if (quantity.compareTo(this.quantityReserved) > 0) {
            throw new StockOperationNotAllowedException(
                    ("Cannot release %s units — only %s units are reserved for item '%s'")
                            .formatted(quantity, this.quantityReserved, itemId));
        }

        this.quantityReserved = scale(this.quantityReserved.subtract(quantity));

        registerEvent(new StockReservationReleasedEvent(getId(), tenantId, companyId, itemId, warehouseId, quantity, this.quantityReserved, getQuantityAvailable(), referenceId));
    }

    public void increaseOnHand(BigDecimal quantity) {
        validatePositive(quantity, "receipt quantity");

        BigDecimal previousOnHand = this.quantityOnHand;
        this.quantityOnHand = scale(this.quantityOnHand.add(quantity));

        registerEvent(new StockLevelChangedEvent(getId(), tenantId, companyId, itemId, warehouseId, previousOnHand, this.quantityOnHand, this.quantityReserved, getQuantityAvailable()));
    }

    public void decreaseOnHand(BigDecimal quantity, boolean fromReservation) {
        validatePositive(quantity, "issue quantity");

        if (!allowNegativeStock && quantity.compareTo(this.quantityOnHand) > 0) {
            throw new StockOperationNotAllowedException(
                    ("Cannot issue %s units — only %s on hand for item '%s'")
                            .formatted(quantity, this.quantityOnHand, itemId));
        }

        BigDecimal previousOnHand = this.quantityOnHand;
        this.quantityOnHand = scale(this.quantityOnHand.subtract(quantity));

        if (fromReservation) {
            BigDecimal releaseQty = quantity.min(this.quantityReserved);
            this.quantityReserved = scale(this.quantityReserved.subtract(releaseQty));
        }

        registerEvent(new StockLevelChangedEvent(getId(), tenantId, companyId, itemId, warehouseId, previousOnHand, this.quantityOnHand, this.quantityReserved, getQuantityAvailable()));

        checkReorderPoint();
    }

    public void adjust(BigDecimal adjustment) {
        if (adjustment == null)
            throw new IllegalArgumentException("adjustment cannot be null");
        if (adjustment.compareTo(BigDecimal.ZERO) == 0) return;

        BigDecimal newOnHand = this.quantityOnHand.add(adjustment);

        if (!allowNegativeStock && newOnHand.compareTo(BigDecimal.ZERO) < 0) {
            throw new StockOperationNotAllowedException(
                    "Adjustment would result in negative stock for item '%s'".formatted(itemId));
        }

        BigDecimal previousOnHand = this.quantityOnHand;
        this.quantityOnHand = scale(newOnHand);

        registerEvent(new StockLevelChangedEvent(getId(), tenantId, companyId, itemId, warehouseId, previousOnHand, this.quantityOnHand, this.quantityReserved, getQuantityAvailable()));

        checkReorderPoint();
    }

    public void setReorderPoint(BigDecimal reorderPoint) {
        if (reorderPoint != null && reorderPoint.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("reorderPoint cannot be negative");
        this.reorderPoint = reorderPoint;
    }

    public BigDecimal getQuantityAvailable() {
        return scale(quantityOnHand.subtract(quantityReserved));
    }

    private void checkReorderPoint() {
        if (reorderPoint != null && getQuantityAvailable().compareTo(reorderPoint) <= 0) {
            registerEvent(new StockLevelBelowReorderPointEvent(getId(), tenantId, companyId, itemId, warehouseId, getQuantityAvailable(), reorderPoint));
        }
    }

    private static void validatePositive(BigDecimal value, String field) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("%s must be positive, got: %s".formatted(field, value));
    }

    private static BigDecimal scale(BigDecimal value) {
        return value.setScale(SCALE, ROUNDING);
    }
}

