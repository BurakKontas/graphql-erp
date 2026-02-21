package tr.kontas.erp.app.purchase.dtos;

import lombok.Data;

@Data
public class CancelPurchaseOrderInput {
    private String orderId;
    private String reason;
}

