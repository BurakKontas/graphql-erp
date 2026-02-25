package tr.kontas.erp.app.tenant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.tenant.validators.UpdateTenantOidcSettingsInputValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validate(validator = UpdateTenantOidcSettingsInputValidator.class)
public class UpdateTenantOidcSettingsInput implements Validatable {
    private String tenantId;
    private String issuer;
    private String audience;
    private String jwkSetUri;
    private Long clockSkewSeconds;
}
