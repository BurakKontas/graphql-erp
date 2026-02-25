package tr.kontas.erp.app.tenant.validators;

import tr.kontas.erp.app.tenant.dtos.UpdateTenantAuthModeInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class UpdateTenantAuthModeInputValidator extends Validator<UpdateTenantAuthModeInput> {
    public UpdateTenantAuthModeInputValidator() {
        ruleFor(UpdateTenantAuthModeInput::getTenantId).notNull().notBlank();
        ruleFor(UpdateTenantAuthModeInput::getAuthMode).notNull().notBlank();
    }
}
