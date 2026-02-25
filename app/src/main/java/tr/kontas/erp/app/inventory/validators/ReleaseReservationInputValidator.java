package tr.kontas.erp.app.inventory.validators;

import tr.kontas.erp.app.inventory.dtos.ReleaseReservationInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class ReleaseReservationInputValidator extends Validator<ReleaseReservationInput> {
    public ReleaseReservationInputValidator() {
        ruleFor(ReleaseReservationInput::getItemId).notNull().notBlank();
        ruleFor(ReleaseReservationInput::getWarehouseId).notNull().notBlank();
        ruleFor(ReleaseReservationInput::getQuantity).notNull();
    }
}
