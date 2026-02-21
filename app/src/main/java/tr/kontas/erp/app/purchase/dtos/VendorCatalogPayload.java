package tr.kontas.erp.app.purchase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class VendorCatalogPayload {
    private String id;
    private String companyId;
    private String vendorId;
    private String vendorName;
    private String currencyCode;
    private String validFrom;
    private String validTo;
    private boolean active;
    private List<VendorCatalogEntryPayload> entries;
}

