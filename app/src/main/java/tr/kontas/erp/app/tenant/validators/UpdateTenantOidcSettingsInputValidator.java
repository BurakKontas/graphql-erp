package tr.kontas.erp.app.tenant.validators;

import tr.kontas.erp.app.tenant.dtos.UpdateTenantOidcSettingsInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class UpdateTenantOidcSettingsInputValidator extends Validator<UpdateTenantOidcSettingsInput> {
    public UpdateTenantOidcSettingsInputValidator() {
        ruleFor(UpdateTenantOidcSettingsInput::getTenantId).notNull().notBlank();
        ruleFor(UpdateTenantOidcSettingsInput::getIssuer).notNull().notBlank();
    }
}
