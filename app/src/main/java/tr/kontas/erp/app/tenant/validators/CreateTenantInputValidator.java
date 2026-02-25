package tr.kontas.erp.app.tenant.validators;

import tr.kontas.erp.app.tenant.dtos.CreateTenantInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateTenantInputValidator extends Validator<CreateTenantInput> {

    private static final String TENANT_CODE_REGEX = "^[a-zA-Z][a-zA-Z0-9_]{2,29}$";

    public CreateTenantInputValidator() {

        ruleFor(CreateTenantInput::getCode)
                .notNull()
                .notBlank()
                .matches(TENANT_CODE_REGEX)
                .withMessage("Code must contain only letters, numbers and underscore (3-30 chars).");

        ruleFor(CreateTenantInput::getName)
                .notNull()
                .notBlank();
    }
}