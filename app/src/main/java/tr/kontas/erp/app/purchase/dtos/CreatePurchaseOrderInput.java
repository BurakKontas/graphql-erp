package tr.kontas.erp.app.purchase.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.purchase.validators.CreatePurchaseOrderInputValidator;

import java.math.BigDecimal;
import java.util.List;

@Data
@Validate(validator = CreatePurchaseOrderInputValidator.class)
public class CreatePurchaseOrderInput implements Validatable {
    private String companyId;
    private String requestId;
    private String vendorId;
    private String vendorName;
    private String orderDate;
    private String expectedDeliveryDate;
    private String currencyCode;
    private String paymentTermCode;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateOrProvince;
    private String postalCode;
    private String countryCode;
    private List<LineInput> lines;

    @Data
    public static class LineInput {
        private String requestLineId;
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal orderedQty;
        private BigDecimal unitPrice;
        private String taxCode;
        private BigDecimal taxRate;
        private String expenseAccountId;
    }
}
