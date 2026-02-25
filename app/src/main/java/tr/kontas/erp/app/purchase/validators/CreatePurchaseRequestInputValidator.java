package tr.kontas.erp.app.purchase.validators;

import tr.kontas.erp.app.purchase.dtos.CreatePurchaseRequestInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreatePurchaseRequestInputValidator extends Validator<CreatePurchaseRequestInput> {
    public CreatePurchaseRequestInputValidator() {
        ruleFor(CreatePurchaseRequestInput::getCompanyId)
                .notNull()
                .notBlank();

        ruleFor(CreatePurchaseRequestInput::getRequestedBy)
                .notNull()
                .notBlank();

        ruleFor(CreatePurchaseRequestInput::getLines)
                .notNull()
                .notEmpty()
                .withMessage("At least one line is required");
    }
}
