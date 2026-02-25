package tr.kontas.erp.app.identity.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.identity.validators.ProvisionExternalIdentityInputValidator;

@Data
@Validate(validator = ProvisionExternalIdentityInputValidator.class)
public class ProvisionExternalIdentityInput implements Validatable {
    private String username;
    private String provider; // LDAP or OIDC
    private String externalId; // e.g. user DN or OIDC subject
}
