package tr.kontas.erp.app.identity.validators;

import tr.kontas.erp.app.identity.dtos.RegisterInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class RegisterInputValidator extends Validator<RegisterInput> {
    public RegisterInputValidator() {
        ruleFor(RegisterInput::getUsername)
                .notNull()
                .notBlank();

        ruleFor(RegisterInput::getPassword)
                .notNull()
                .notBlank()
                .strongPassword(6, 50);
    }
}
