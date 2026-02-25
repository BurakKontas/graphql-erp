package tr.kontas.erp.app.purchase.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.purchase.validators.CreateVendorCatalogInputValidator;

import java.math.BigDecimal;
import java.util.List;

@Data
@Validate(validator = CreateVendorCatalogInputValidator.class)
public class CreateVendorCatalogInput implements Validatable {
    private String companyId;
    private String vendorId;
    private String vendorName;
    private String currencyCode;
    private String validFrom;
    private String validTo;
    private List<EntryInput> entries;

    @Data
    public static class EntryInput {
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal unitPrice;
        private BigDecimal minimumOrderQty;
        private BigDecimal priceBreakQty;
        private BigDecimal priceBreakUnitPrice;
    }
}
