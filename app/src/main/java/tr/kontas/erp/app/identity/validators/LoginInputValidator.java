package tr.kontas.erp.app.identity.validators;

import org.springframework.stereotype.Component;
import tr.kontas.erp.app.identity.dtos.LoginInput;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.fluentvalidation.validation.Validator;

public class LoginInputValidator extends Validator<LoginInput> {
    public LoginInputValidator() {
        ruleFor(_ -> TenantContext.get())
                .notNull();

        ruleFor(LoginInput::getIdToken)
            .when(_ -> TenantContext.get().getAuthMode() == AuthProviderType.OIDC)
            .notNull();

        ruleFor(LoginInput::getUsername)
            .unless(_ -> TenantContext.get().getAuthMode() == AuthProviderType.OIDC)
            .notNull();

        ruleFor(LoginInput::getPassword)
            .unless(_ -> TenantContext.get().getAuthMode() == AuthProviderType.OIDC)
            .notNull();
    }
}
