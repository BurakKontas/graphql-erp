package tr.kontas.erp.purchase.domain.purchaserequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public class PurchaseRequestLine {
    private final PurchaseRequestLineId id;
    private final PurchaseRequestId requestId;
    private final String itemId;
    private final String itemDescription;
    private final String unitCode;
    private final BigDecimal quantity;
    private final String preferredVendorId;
    private final String notes;
}
