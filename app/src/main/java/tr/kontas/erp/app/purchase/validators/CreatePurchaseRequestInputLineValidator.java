package tr.kontas.erp.app.purchase.validators;

import tr.kontas.erp.app.purchase.dtos.CreatePurchaseRequestInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreatePurchaseRequestInputLineValidator extends Validator<CreatePurchaseRequestInput.LineInput> {
    public CreatePurchaseRequestInputLineValidator() {
        ruleFor(CreatePurchaseRequestInput.LineInput::getItemId)
                .notNull()
                .notBlank();

        ruleFor(CreatePurchaseRequestInput.LineInput::getQuantity)
                .notNull()
                .greaterThan(0)
                .withMessage("Quantity must be greater than zero");
    }
}
