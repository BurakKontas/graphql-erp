package tr.kontas.erp.app.purchase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PurchaseRequestLinePayload {
    private String id;
    private String itemId;
    private String itemDescription;
    private String unitCode;
    private BigDecimal quantity;
    private String preferredVendorId;
    private String notes;
}

