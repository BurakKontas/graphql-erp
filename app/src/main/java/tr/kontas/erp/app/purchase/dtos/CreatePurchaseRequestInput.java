package tr.kontas.erp.app.purchase.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.purchase.validators.CreatePurchaseRequestInputValidator;

import java.math.BigDecimal;
import java.util.List;

@Data
@Validate(validator = CreatePurchaseRequestInputValidator.class)
public class CreatePurchaseRequestInput implements Validatable {
    private String companyId;
    private String requestedBy;
    private String requestDate;
    private String neededBy;
    private String notes;
    private List<LineInput> lines;

    @Data
    public static class LineInput {
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal quantity;
        private String preferredVendorId;
        private String notes;
    }
}
