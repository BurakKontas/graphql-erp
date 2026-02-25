package tr.kontas.erp.app.inventory.validators;

import tr.kontas.erp.app.inventory.dtos.CreateWarehouseInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateWarehouseInputValidator extends Validator<CreateWarehouseInput> {
    public CreateWarehouseInputValidator() {
        ruleFor(CreateWarehouseInput::getCompanyId)
                .notNull()
                .notBlank();

        ruleFor(CreateWarehouseInput::getCode)
                .notNull()
                .notBlank();

        ruleFor(CreateWarehouseInput::getName)
                .notNull()
                .notBlank();
    }
}
