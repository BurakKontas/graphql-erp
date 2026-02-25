package tr.kontas.erp.app.inventory.validators;

import tr.kontas.erp.app.inventory.dtos.CreateCategoryInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateCategoryInputValidator extends Validator<CreateCategoryInput> {
    public CreateCategoryInputValidator() {
        ruleFor(CreateCategoryInput::getCompanyId)
                .notNull()
                .notBlank();

        ruleFor(CreateCategoryInput::getName)
                .notNull()
                .notBlank()
                .withMessage("Category name is required");
    }
}
