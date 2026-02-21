package tr.kontas.erp.sales.domain.salesorder;

import lombok.Getter;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.domain.shared.Quantity;
import tr.kontas.erp.core.kernel.domain.model.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class SalesOrderLine extends Entity<SalesOrderLineId> {
    private static final int PRICE_SCALE = 6;
    private static final int AMOUNT_SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final SalesOrderId orderId;
    private final String itemId;
    private final String itemDescription;
    private final String unitCode;
    private final int sequence;
    private Quantity quantity;
    private BigDecimal unitPrice;

    private final Tax tax;

    private BigDecimal lineTotal;
    private BigDecimal taxAmount;
    private BigDecimal lineTotalWithTax;

    public SalesOrderLine(
            SalesOrderLineId id,
            SalesOrderId orderId,
            int sequence,
            String itemId,
            String itemDescription,
            String unitCode,
            Quantity quantity,
            BigDecimal unitPrice,
            Tax tax
    ) {
        super(id);

        if (orderId == null)
            throw new IllegalArgumentException("orderId cannot be null");
        if (itemDescription == null || itemDescription.isBlank())
            throw new IllegalArgumentException("itemDescription cannot be blank");
        if (unitCode == null || unitCode.isBlank())
            throw new IllegalArgumentException("unitCode cannot be blank");

        this.orderId = orderId;
        this.sequence = sequence;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.unitCode = unitCode;
        this.tax = tax;

        recalculate(quantity, unitPrice);
    }

    void update(Quantity newQuantity, BigDecimal newUnitPrice) {
        if (newUnitPrice == null || newUnitPrice.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("unitPrice cannot be negative");
        recalculate(newQuantity, newUnitPrice);
    }

    private void recalculate(Quantity quantity, BigDecimal unitPrice) {
        if (quantity == null)
            throw new IllegalArgumentException("quantity cannot be null");
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("unitPrice cannot be negative");

        this.quantity = quantity;
        this.unitPrice = unitPrice.setScale(PRICE_SCALE, ROUNDING);

        this.lineTotal = this.unitPrice
                .multiply(quantity.getValue())
                .setScale(AMOUNT_SCALE, ROUNDING);

        this.taxAmount = this.lineTotal
                .multiply(tax.getRate().getValue())
                .setScale(AMOUNT_SCALE, ROUNDING);

        this.lineTotalWithTax = this.lineTotal
                .add(taxAmount)
                .setScale(AMOUNT_SCALE, ROUNDING);
    }
}