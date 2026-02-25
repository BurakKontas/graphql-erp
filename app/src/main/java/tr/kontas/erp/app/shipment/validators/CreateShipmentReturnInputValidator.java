package tr.kontas.erp.app.shipment.validators;

import tr.kontas.erp.app.shipment.dtos.CreateShipmentReturnInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateShipmentReturnInputValidator extends Validator<CreateShipmentReturnInput> {
    public CreateShipmentReturnInputValidator() {
        ruleFor(CreateShipmentReturnInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateShipmentReturnInput::getShipmentId).notNull().notBlank();
        ruleFor(CreateShipmentReturnInput::getLines).notNull().notEmpty().withMessage("At least one line is required");
    }
}
