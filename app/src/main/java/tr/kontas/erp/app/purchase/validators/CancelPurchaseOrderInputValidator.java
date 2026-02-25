package tr.kontas.erp.app.purchase.validators;

import tr.kontas.erp.app.purchase.dtos.CancelPurchaseOrderInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CancelPurchaseOrderInputValidator extends Validator<CancelPurchaseOrderInput> {
    public CancelPurchaseOrderInputValidator() {
        ruleFor(CancelPurchaseOrderInput::getOrderId)
                .notNull()
                .notBlank();

        ruleFor(CancelPurchaseOrderInput::getReason)
                .unless(r -> r == null)
                .maxLength(1000);
    }
}
