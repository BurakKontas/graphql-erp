package tr.kontas.erp.shipment.domain.shipmentreturn;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class ShipmentReturnLine extends Entity<ShipmentReturnLineId> {
    private static final int QTY_SCALE = 4;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final String shipmentLineId;
    private final String itemId;
    private final String itemDescription;
    private final String unitCode;
    private final BigDecimal quantity;
    private final String lineReason; // nullable — per-line explanation

    public ShipmentReturnLine(
            ShipmentReturnLineId id,
            String shipmentLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal quantity,
            String lineReason
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

        this.shipmentLineId = shipmentLineId;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.unitCode = unitCode;
        this.quantity = quantity.setScale(QTY_SCALE, ROUNDING);
        this.lineReason = lineReason;
    }
}

