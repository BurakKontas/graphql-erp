package tr.kontas.erp.app.purchase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseReturnPayload {
    private String id;
    private String companyId;
    private String returnNumber;
    private String purchaseOrderId;
    private String goodsReceiptId;
    private String vendorId;
    private String warehouseId;
    private String returnDate;
    private String reason;
    private String status;
    private List<PurchaseReturnLinePayload> lines;
}

