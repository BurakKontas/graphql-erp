package tr.kontas.erp.finance.domain.creditnote;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class CreditNoteLine extends Entity<CreditNoteLineId> {
    private static final int AMOUNT_SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final CreditNoteId creditNoteId;
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

    public CreditNoteLine(CreditNoteLineId id,
                          CreditNoteId creditNoteId,
                          String itemId,
                          String itemDescription,
                          String unitCode,
                          BigDecimal quantity,
                          BigDecimal unitPrice,
                          String taxCode,
                          BigDecimal taxRate) {
        super(id);

        if (creditNoteId == null) throw new IllegalArgumentException("creditNoteId cannot be null");

        this.creditNoteId = creditNoteId;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.unitCode = unitCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.taxCode = taxCode;
        this.taxRate = taxRate != null ? taxRate : BigDecimal.ZERO;

        this.lineTotal = this.unitPrice.multiply(this.quantity).setScale(AMOUNT_SCALE, ROUNDING);
        this.taxAmount = this.lineTotal.multiply(this.taxRate).setScale(AMOUNT_SCALE, ROUNDING);
        this.lineTotalWithTax = this.lineTotal.add(this.taxAmount).setScale(AMOUNT_SCALE, ROUNDING);
    }
}

