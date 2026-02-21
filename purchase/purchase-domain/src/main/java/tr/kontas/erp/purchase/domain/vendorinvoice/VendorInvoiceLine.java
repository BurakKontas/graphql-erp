package tr.kontas.erp.purchase.domain.vendorinvoice;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class VendorInvoiceLine {
    private final VendorInvoiceLineId id;
    private final VendorInvoiceId invoiceId;
    private final String poLineId;
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
    private final String accountId;

    public VendorInvoiceLine(VendorInvoiceLineId id, VendorInvoiceId invoiceId,
                             String poLineId, String itemId, String itemDescription,
                             String unitCode, BigDecimal quantity, BigDecimal unitPrice,
                             String taxCode, BigDecimal taxRate, String accountId) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.poLineId = poLineId;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.unitCode = unitCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.taxCode = taxCode;
        this.taxRate = taxRate != null ? taxRate : BigDecimal.ZERO;
        this.accountId = accountId;
        this.lineTotal = quantity.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);
        this.taxAmount = this.lineTotal.multiply(this.taxRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        this.lineTotalWithTax = this.lineTotal.add(this.taxAmount);
    }
}

