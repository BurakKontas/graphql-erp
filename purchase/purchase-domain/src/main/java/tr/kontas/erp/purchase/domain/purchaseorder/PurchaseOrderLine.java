package tr.kontas.erp.purchase.domain.purchaseorder;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class PurchaseOrderLine {

    private final PurchaseOrderLineId id;
    private final PurchaseOrderId orderId;
    private final String requestLineId;
    private final String itemId;
    private final String itemDescription;
    private final String unitCode;
    private final BigDecimal orderedQty;
    private BigDecimal receivedQty;
    private final BigDecimal unitPrice;
    private final String taxCode;
    private final BigDecimal taxRate;
    private final BigDecimal lineTotal;
    private final BigDecimal taxAmount;
    private final String expenseAccountId;

    public PurchaseOrderLine(PurchaseOrderLineId id, PurchaseOrderId orderId,
                             String requestLineId, String itemId, String itemDescription,
                             String unitCode, BigDecimal orderedQty, BigDecimal receivedQty,
                             BigDecimal unitPrice, String taxCode, BigDecimal taxRate,
                             String expenseAccountId) {
        this.id = id;
        this.orderId = orderId;
        this.requestLineId = requestLineId;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.unitCode = unitCode;
        this.orderedQty = orderedQty;
        this.receivedQty = receivedQty != null ? receivedQty : BigDecimal.ZERO;
        this.unitPrice = unitPrice;
        this.taxCode = taxCode;
        this.taxRate = taxRate != null ? taxRate : BigDecimal.ZERO;
        this.expenseAccountId = expenseAccountId;
        this.lineTotal = orderedQty.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);
        this.taxAmount = this.lineTotal.multiply(this.taxRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }


    public BigDecimal getLineTotal() {
        return lineTotal;
    }


    public BigDecimal getTaxAmount() {
        return taxAmount;
    }


    public BigDecimal getRemainingQty() {
        return orderedQty.subtract(receivedQty);
    }


    public boolean isFullyReceived() {
        return getRemainingQty().compareTo(BigDecimal.ZERO) <= 0;
    }

    void addReceived(BigDecimal quantity) {
        this.receivedQty = this.receivedQty.add(quantity);
        if (this.receivedQty.compareTo(orderedQty) > 0) {
            throw new IllegalStateException("Received quantity exceeds ordered quantity for line: " + id);
        }
    }
}

