package tr.kontas.erp.app.shipment.validators;

import tr.kontas.erp.app.shipment.dtos.SetTrackingInfoInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class SetTrackingInfoInputValidator extends Validator<SetTrackingInfoInput> {
    public SetTrackingInfoInputValidator() {
        ruleFor(SetTrackingInfoInput::getShipmentId).notNull().notBlank();
        ruleFor(SetTrackingInfoInput::getTrackingNumber).notNull().notBlank();
    }
}
