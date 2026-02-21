package tr.kontas.erp.app.purchase.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateGoodsReceiptInput {
    private String companyId;
    private String purchaseOrderId;
    private String vendorId;
    private String warehouseId;
    private String receiptDate;
    private String vendorDeliveryNote;
    private List<LineInput> lines;

    @Data
    public static class LineInput {
        private String poLineId;
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal quantity;
        private String batchNote;
    }
}

