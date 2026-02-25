package tr.kontas.erp.app.purchase.validators;

import tr.kontas.erp.app.purchase.dtos.CreateGoodsReceiptInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateGoodsReceiptInputValidator extends Validator<CreateGoodsReceiptInput> {
    public CreateGoodsReceiptInputValidator() {
        ruleFor(CreateGoodsReceiptInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateGoodsReceiptInput::getPurchaseOrderId).notNull().notBlank();
        ruleFor(CreateGoodsReceiptInput::getLines).notNull().notEmpty().withMessage("At least one line is required");
    }
}
