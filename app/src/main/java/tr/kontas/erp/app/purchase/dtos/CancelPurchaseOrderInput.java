package tr.kontas.erp.app.purchase.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.purchase.validators.CancelPurchaseOrderInputValidator;

@Data
@Validate(validator = CancelPurchaseOrderInputValidator.class)
public class CancelPurchaseOrderInput implements Validatable {
    private String orderId;
    private String reason;
}
