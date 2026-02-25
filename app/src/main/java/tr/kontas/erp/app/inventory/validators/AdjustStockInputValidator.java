package tr.kontas.erp.app.inventory.validators;

import tr.kontas.erp.app.inventory.dtos.AdjustStockInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class AdjustStockInputValidator extends Validator<AdjustStockInput> {
    public AdjustStockInputValidator() {
        ruleFor(AdjustStockInput::getItemId).notNull().notBlank();
        ruleFor(AdjustStockInput::getWarehouseId).notNull().notBlank();
        ruleFor(AdjustStockInput::getAdjustment).notNull();
    }
}
