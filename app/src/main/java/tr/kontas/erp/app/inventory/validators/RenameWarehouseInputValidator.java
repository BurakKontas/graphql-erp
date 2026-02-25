package tr.kontas.erp.app.inventory.validators;

import tr.kontas.erp.app.inventory.dtos.RenameWarehouseInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class RenameWarehouseInputValidator extends Validator<RenameWarehouseInput> {
    public RenameWarehouseInputValidator() {
        ruleFor(RenameWarehouseInput::getWarehouseId)
                .notNull()
                .notBlank();

        ruleFor(RenameWarehouseInput::getName)
                .notNull()
                .notBlank();
    }
}
