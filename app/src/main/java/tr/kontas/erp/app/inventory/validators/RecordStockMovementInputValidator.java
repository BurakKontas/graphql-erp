package tr.kontas.erp.app.inventory.validators;

import tr.kontas.erp.app.inventory.dtos.RecordStockMovementInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class RecordStockMovementInputValidator extends Validator<RecordStockMovementInput> {
    public RecordStockMovementInputValidator() {
        ruleFor(RecordStockMovementInput::getCompanyId).notNull().notBlank();
        ruleFor(RecordStockMovementInput::getItemId).notNull().notBlank();
        ruleFor(RecordStockMovementInput::getQuantity).notNull();
    }
}
