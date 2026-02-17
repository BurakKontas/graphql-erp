package tr.kontas.erp.core.application.identity;

import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;

public interface AuthenticationProvider {
    boolean supports(AuthProviderType provider);

    AuthenticationResult authenticate(AuthenticationCommand command);
}
