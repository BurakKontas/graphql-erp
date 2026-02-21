package tr.kontas.erp.app.purchase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class GoodsReceiptPayload {
    private String id;
    private String companyId;
    private String receiptNumber;
    private String purchaseOrderId;
    private String vendorId;
    private String warehouseId;
    private String receiptDate;
    private String status;
    private String vendorDeliveryNote;
    private List<GoodsReceiptLinePayload> lines;
}

