package tr.kontas.erp.purchase.domain.purchasereturn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public class PurchaseReturnLine {
    private final PurchaseReturnLineId id;
    private final PurchaseReturnId returnId;
    private final String receiptLineId;
    private final String itemId;
    private final String itemDescription;
    private final String unitCode;
    private final BigDecimal quantity;
    private final String lineReason;
}
