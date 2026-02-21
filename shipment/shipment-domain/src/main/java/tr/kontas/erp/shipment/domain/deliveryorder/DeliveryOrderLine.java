package tr.kontas.erp.shipment.domain.deliveryorder;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Entity;
import tr.kontas.erp.shipment.domain.exception.OvershotDeliveryException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class DeliveryOrderLine extends Entity<DeliveryOrderLineId> {
    private static final int QTY_SCALE = 4;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final String salesOrderLineId;
    private final String itemId;
    private final String itemDescription;
    private final String unitCode;
    private final BigDecimal orderedQty;
    private BigDecimal shippedQty;

    public DeliveryOrderLine(
            DeliveryOrderLineId id,
            String salesOrderLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal orderedQty
    ) {
        super(id);

        if (itemId == null || itemId.isBlank())
            throw new IllegalArgumentException("itemId cannot be blank");
        if (itemDescription == null || itemDescription.isBlank())
            throw new IllegalArgumentException("itemDescription cannot be blank");
        if (unitCode == null || unitCode.isBlank())
            throw new IllegalArgumentException("unitCode cannot be blank");
        if (orderedQty == null || orderedQty.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("orderedQty must be positive");

        this.salesOrderLineId = salesOrderLineId;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.unitCode = unitCode;
        this.orderedQty = orderedQty.setScale(QTY_SCALE, ROUNDING);
        this.shippedQty = BigDecimal.ZERO.setScale(QTY_SCALE, ROUNDING);
    }

    /** Reconstitution constructor */
    public DeliveryOrderLine(
            DeliveryOrderLineId id,
            String salesOrderLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal orderedQty,
            BigDecimal shippedQty
    ) {
        super(id);
        this.salesOrderLineId = salesOrderLineId;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.unitCode = unitCode;
        this.orderedQty = orderedQty.setScale(QTY_SCALE, ROUNDING);
        this.shippedQty = shippedQty != null ? shippedQty.setScale(QTY_SCALE, ROUNDING)
                : BigDecimal.ZERO.setScale(QTY_SCALE, ROUNDING);
    }

    void addShipped(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Shipped quantity must be positive");
        }
        BigDecimal newShipped = this.shippedQty.add(quantity).setScale(QTY_SCALE, ROUNDING);
        if (newShipped.compareTo(orderedQty) > 0) {
            throw new OvershotDeliveryException(getId(), orderedQty, newShipped);
        }
        this.shippedQty = newShipped;
    }

    public BigDecimal getRemainingQty() {
        return orderedQty.subtract(shippedQty).setScale(QTY_SCALE, ROUNDING);
    }

    public boolean isFullyShipped() {
        return getRemainingQty().compareTo(BigDecimal.ZERO) == 0;
    }
}

