package tr.kontas.erp.shipment.domain.shipment;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class ShipmentLine extends Entity<ShipmentLineId> {
    private static final int QTY_SCALE = 4;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final String deliveryOrderLineId;
    private final String itemId;
    private final String itemDescription;
    private final String unitCode;
    private final BigDecimal quantity;

    public ShipmentLine(
            ShipmentLineId id,
            String deliveryOrderLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal quantity
    ) {
        super(id);

        if (itemId == null || itemId.isBlank())
            throw new IllegalArgumentException("itemId cannot be blank");
        if (itemDescription == null || itemDescription.isBlank())
            throw new IllegalArgumentException("itemDescription cannot be blank");
        if (unitCode == null || unitCode.isBlank())
            throw new IllegalArgumentException("unitCode cannot be blank");
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("quantity must be positive");

        this.deliveryOrderLineId = deliveryOrderLineId;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.unitCode = unitCode;
        this.quantity = quantity.setScale(QTY_SCALE, ROUNDING);
    }
}

