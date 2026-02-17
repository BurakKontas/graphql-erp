package tr.kontas.erp.core.application.identity;

import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.identity.valueobjects.UserName;

import java.util.Map;

public record AuthenticationCommand(
        String tenantId,
        AuthProviderType provider, // LOCAL, OIDC, LDAP
        UserName username,
        String credential,
        Map<String, Object> attributes
) {
}