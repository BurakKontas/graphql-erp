package tr.kontas.erp.core.platform.service.identity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.application.identity.AuthenticationCommand;
import tr.kontas.erp.core.application.identity.AuthenticationProvider;
import tr.kontas.erp.core.application.identity.AuthenticationResult;
import tr.kontas.erp.core.application.identity.PasswordEncoder;
import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.identity.repositories.UserRepository;
import tr.kontas.erp.core.domain.identity.valueobjects.ExternalIdentity;
import tr.kontas.erp.core.domain.identity.valueobjects.UserName;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class LocalAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(AuthProviderType provider) {
        return provider == AuthProviderType.LOCAL;
    }

    @Override
    public AuthenticationResult authenticate(AuthenticationCommand command) {

        String tenantId = command.tenantId();
        String usernameStr = (String) command.attributes().get("username");
        String passwordStr = (String) command.attributes().get("password");

        if (tenantId == null || usernameStr == null || passwordStr == null) {
            throw new IllegalArgumentException("Missing credentials");
        }

        var tenantUUID = TenantId.of(tenantId);
        var username = new UserName(usernameStr);

        UserAccount user = userRepository
                .findByUsername(tenantUUID, username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.isActive()) throw new SecurityException("User inactive");
        if (user.isLocked()) throw new SecurityException("User locked");

        if (!passwordEncoder.matches(passwordStr, user.getPassword().getValue())) {
            throw new SecurityException("Invalid password");
        }

        return new AuthenticationResult(
                user.getId().asUUID(),
                username,
                new ExternalIdentity(AuthProviderType.LOCAL, user.getId().getValue().toString()),
                Set.of()
        );
    }
}


