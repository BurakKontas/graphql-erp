package tr.kontas.erp.app.identity.dtos;

import lombok.Data;

@Data
public class ProvisionExternalIdentityInput {
    private String username;
    private String provider; // LDAP or OIDC
    private String externalId; // e.g. user DN or OIDC subject
}
