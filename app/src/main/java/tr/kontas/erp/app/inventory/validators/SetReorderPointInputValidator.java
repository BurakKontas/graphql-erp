package tr.kontas.erp.app.inventory.validators;

import tr.kontas.erp.app.inventory.dtos.SetReorderPointInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class SetReorderPointInputValidator extends Validator<SetReorderPointInput> {
    public SetReorderPointInputValidator() {
        ruleFor(SetReorderPointInput::getStockLevelId).notNull().notBlank();
        ruleFor(SetReorderPointInput::getReorderPoint).notNull();
    }
}
