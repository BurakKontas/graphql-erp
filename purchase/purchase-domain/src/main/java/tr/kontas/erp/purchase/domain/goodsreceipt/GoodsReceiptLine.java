package tr.kontas.erp.purchase.domain.goodsreceipt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public class GoodsReceiptLine {
    private final GoodsReceiptLineId id;
    private final GoodsReceiptId receiptId;
    private final String poLineId;
    private final String itemId;
    private final String itemDescription;
    private final String unitCode;
    private final BigDecimal quantity;
    private final String batchNote;
}
