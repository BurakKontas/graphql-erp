package tr.kontas.erp.app.inventory.validators;

import tr.kontas.erp.app.inventory.dtos.RenameCategoryInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class RenameCategoryInputValidator extends Validator<RenameCategoryInput> {
    public RenameCategoryInputValidator() {
        ruleFor(RenameCategoryInput::getCategoryId)
                .notNull()
                .notBlank();

        ruleFor(RenameCategoryInput::getName)
                .notNull()
                .notBlank();
    }
}
