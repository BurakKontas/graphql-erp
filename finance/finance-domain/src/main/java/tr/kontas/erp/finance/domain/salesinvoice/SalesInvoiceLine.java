package tr.kontas.erp.finance.domain.salesinvoice;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class SalesInvoiceLine extends Entity<SalesInvoiceLineId> {
    private static final int AMOUNT_SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final SalesInvoiceId invoiceId;
    private final String salesOrderLineId;
    private final String itemId;
    private final String itemDescription;
    private final String unitCode;
    private final BigDecimal quantity;
    private final BigDecimal unitPrice;
    private final String taxCode;
    private final BigDecimal taxRate;
    private final BigDecimal lineTotal;
    private final BigDecimal taxAmount;
    private final BigDecimal lineTotalWithTax;
    private final String revenueAccountId;

    public SalesInvoiceLine(SalesInvoiceLineId id,
                            SalesInvoiceId invoiceId,
                            String salesOrderLineId,
                            String itemId,
                            String itemDescription,
                            String unitCode,
                            BigDecimal quantity,
                            BigDecimal unitPrice,
                            String taxCode,
                            BigDecimal taxRate,
                            String revenueAccountId) {
        super(id);

        if (invoiceId == null) throw new IllegalArgumentException("invoiceId cannot be null");
        if (itemDescription == null || itemDescription.isBlank())
            throw new IllegalArgumentException("itemDescription cannot be blank");
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("quantity must be positive");
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("unitPrice cannot be negative");

        this.invoiceId = invoiceId;
        this.salesOrderLineId = salesOrderLineId;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.unitCode = unitCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.taxCode = taxCode;
        this.taxRate = taxRate != null ? taxRate : BigDecimal.ZERO;
        this.revenueAccountId = revenueAccountId;

        this.lineTotal = this.unitPrice.multiply(this.quantity).setScale(AMOUNT_SCALE, ROUNDING);
        this.taxAmount = this.lineTotal.multiply(this.taxRate).setScale(AMOUNT_SCALE, ROUNDING);
        this.lineTotalWithTax = this.lineTotal.add(this.taxAmount).setScale(AMOUNT_SCALE, ROUNDING);
    }
}

