package tr.kontas.erp.app.purchase.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreatePurchaseReturnInput {
    private String companyId;
    private String purchaseOrderId;
    private String goodsReceiptId;
    private String vendorId;
    private String warehouseId;
    private String returnDate;
    private String reason;
    private List<LineInput> lines;

    @Data
    public static class LineInput {
        private String receiptLineId;
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal quantity;
        private String lineReason;
    }
}

