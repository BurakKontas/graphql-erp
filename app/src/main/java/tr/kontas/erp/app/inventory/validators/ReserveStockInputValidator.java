package tr.kontas.erp.app.inventory.validators;

import tr.kontas.erp.app.inventory.dtos.ReserveStockInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class ReserveStockInputValidator extends Validator<ReserveStockInput> {
    public ReserveStockInputValidator() {
        ruleFor(ReserveStockInput::getItemId).notNull().notBlank();
        ruleFor(ReserveStockInput::getWarehouseId).notNull().notBlank();
        ruleFor(ReserveStockInput::getQuantity).notNull();
    }
}
