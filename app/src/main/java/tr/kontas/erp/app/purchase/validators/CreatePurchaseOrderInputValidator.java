package tr.kontas.erp.app.purchase.validators;

import tr.kontas.erp.app.purchase.dtos.CreatePurchaseOrderInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreatePurchaseOrderInputValidator extends Validator<CreatePurchaseOrderInput> {
    public CreatePurchaseOrderInputValidator() {
        ruleFor(CreatePurchaseOrderInput::getCompanyId).notNull().notBlank();
        ruleFor(CreatePurchaseOrderInput::getVendorId).notNull().notBlank();
        ruleFor(CreatePurchaseOrderInput::getLines).notNull().notEmpty().withMessage("At least one line is required");
    }
}
