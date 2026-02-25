package tr.kontas.erp.app.identity.validators;

import tr.kontas.erp.app.identity.dtos.ProvisionExternalIdentityInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class ProvisionExternalIdentityInputValidator extends Validator<ProvisionExternalIdentityInput> {
    public ProvisionExternalIdentityInputValidator() {
        ruleFor(ProvisionExternalIdentityInput::getProvider)
                .notNull()
                .notBlank();

        ruleFor(ProvisionExternalIdentityInput::getExternalId)
                .notNull()
                .notBlank();
    }
}
