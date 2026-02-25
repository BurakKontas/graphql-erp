package tr.kontas.erp.app.shipment.validators;

import tr.kontas.erp.app.shipment.dtos.CreateShipmentInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateShipmentInputValidator extends Validator<CreateShipmentInput> {
    public CreateShipmentInputValidator() {
        ruleFor(CreateShipmentInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateShipmentInput::getWarehouseId).notNull().notBlank();
        ruleFor(CreateShipmentInput::getLines).notNull().notEmpty().withMessage("At least one line is required");
    }
}
