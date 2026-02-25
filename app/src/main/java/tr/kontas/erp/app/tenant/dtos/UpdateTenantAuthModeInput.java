package tr.kontas.erp.app.tenant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.tenant.validators.UpdateTenantAuthModeInputValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validate(validator = UpdateTenantAuthModeInputValidator.class)
public class UpdateTenantAuthModeInput implements Validatable {
    private String tenantId;
    private String authMode;
}
