package tr.kontas.erp.app.inventory.validators;

import tr.kontas.erp.app.inventory.dtos.UpdateItemInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class UpdateItemInputValidator extends Validator<UpdateItemInput> {
    public UpdateItemInputValidator() {
        ruleFor(UpdateItemInput::getItemId)
                .notNull()
                .notBlank();

        ruleFor(UpdateItemInput::getName)
                .unless(n -> n == null)
                .notBlank();
    }
}
