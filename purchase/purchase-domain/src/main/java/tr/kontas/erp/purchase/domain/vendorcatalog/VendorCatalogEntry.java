package tr.kontas.erp.purchase.domain.vendorcatalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public class VendorCatalogEntry {
    private final VendorCatalogEntryId id;
    private final VendorCatalogId catalogId;
    private final String itemId;
    private final String itemDescription;
    private final String unitCode;
    private final BigDecimal unitPrice;
    private final BigDecimal minimumOrderQty;
    private final BigDecimal priceBreakQty;
    private final BigDecimal priceBreakUnitPrice;
}
