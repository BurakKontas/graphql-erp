package tr.kontas.erp.app.purchase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class VendorCatalogEntryPayload {
    private String id;
    private String itemId;
    private String itemDescription;
    private String unitCode;
    private BigDecimal unitPrice;
    private BigDecimal minimumOrderQty;
    private BigDecimal priceBreakQty;
    private BigDecimal priceBreakUnitPrice;
}

