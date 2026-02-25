package tr.kontas.erp.app.reference.validators;

import tr.kontas.erp.app.reference.dtos.CreateTaxInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateTaxInputValidator extends Validator<CreateTaxInput> {
    public CreateTaxInputValidator() {
        ruleFor(CreateTaxInput::getCompanyId)
                .notNull()
                .notBlank();

        ruleFor(CreateTaxInput::getCode)
                .notNull()
                .notBlank();

        ruleFor(CreateTaxInput::getName)
                .notNull()
                .notBlank();

        ruleFor(CreateTaxInput::getRate)
                .notNull()
                .greaterThanOrEqualTo(0);
    }
}
