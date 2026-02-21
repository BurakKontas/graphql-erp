package tr.kontas.erp.crm.domain.quote;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class QuoteLine {

    private final QuoteLineId id;
    private String itemId;
    private String itemDescription;
    private String unitCode;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountRate;
    private String taxCode;
    private BigDecimal taxRate;
    private BigDecimal lineTotal;
    private BigDecimal taxAmount;
    private BigDecimal lineTotalWithTax;

    public QuoteLine(QuoteLineId id, String itemId, String itemDescription,
                     String unitCode, BigDecimal quantity, BigDecimal unitPrice,
                     BigDecimal discountRate, String taxCode, BigDecimal taxRate) {
        this.id = id;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.unitCode = unitCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountRate = discountRate;
        this.taxCode = taxCode;
        this.taxRate = taxRate;
        recalculate();
    }


    public void recalculate() {
        BigDecimal gross = quantity.multiply(unitPrice);
        if (discountRate != null && discountRate.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = gross.multiply(discountRate).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            this.lineTotal = gross.subtract(discount);
        } else {
            this.lineTotal = gross;
        }
        if (taxRate != null && taxRate.compareTo(BigDecimal.ZERO) > 0) {
            this.taxAmount = lineTotal.multiply(taxRate).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            this.taxAmount = BigDecimal.ZERO;
        }
        this.lineTotalWithTax = lineTotal.add(taxAmount);
    }
}

