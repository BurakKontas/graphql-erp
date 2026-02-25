package tr.kontas.erp.app.purchase.validators;

import tr.kontas.erp.app.purchase.dtos.CreatePurchaseReturnInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreatePurchaseReturnInputValidator extends Validator<CreatePurchaseReturnInput> {
    public CreatePurchaseReturnInputValidator() {
        ruleFor(CreatePurchaseReturnInput::getCompanyId).notNull().notBlank();
        ruleFor(CreatePurchaseReturnInput::getReturnDate).notNull().notBlank();
        ruleFor(CreatePurchaseReturnInput::getLines).notNull().notEmpty();
    }
}
