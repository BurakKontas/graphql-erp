package tr.kontas.erp.app.shipment.validators;

import tr.kontas.erp.app.shipment.dtos.CreateDeliveryOrderInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateDeliveryOrderInputValidator extends Validator<CreateDeliveryOrderInput> {
    public CreateDeliveryOrderInputValidator() {
        ruleFor(CreateDeliveryOrderInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateDeliveryOrderInput::getSalesOrderId).notNull().notBlank();
        ruleFor(CreateDeliveryOrderInput::getLines).notNull().notEmpty().withMessage("At least one line is required");
    }
}
