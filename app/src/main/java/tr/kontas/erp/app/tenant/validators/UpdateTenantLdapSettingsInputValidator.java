package tr.kontas.erp.app.tenant.validators;

import tr.kontas.erp.app.tenant.dtos.UpdateTenantLdapSettingsInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class UpdateTenantLdapSettingsInputValidator extends Validator<UpdateTenantLdapSettingsInput> {
    public UpdateTenantLdapSettingsInputValidator() {
        ruleFor(UpdateTenantLdapSettingsInput::getTenantId).notNull().notBlank();
        ruleFor(UpdateTenantLdapSettingsInput::getUrls).notNull().notEmpty();
        ruleFor(UpdateTenantLdapSettingsInput::getBaseDn).notNull().notBlank();
    }
}
