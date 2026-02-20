package tr.kontas.erp.core.platform.service.identity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.identity.*;
import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.identity.repositories.UserRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationApplicationService {

    private final AuthenticationProviderRegistry providerRegistry;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public List<AuthToken> authenticate(AuthenticationCommand command) {

        AuthenticationProvider provider = providerRegistry.getProvider(command.provider());

        AuthenticationResult result = provider.authenticate(command);

        UserAccount user = resolveUser(command.tenantId(), result);

        if (!user.isActive()) {
            throw new IllegalStateException("User is inactive");
        }

        if (user.isLocked()) {
            throw new IllegalStateException("User is locked");
        }

        TokenResult token = jwtService.generate(user);

        AuthToken accessToken = AuthToken.bearer(token.accessToken(), token.accessTokenExpiresAt());
        AuthToken refreshToken = AuthToken.bearer(token.refreshToken(), token.refreshTokenExpiresAt());

        return List.of(accessToken, refreshToken);
    }

    private UserAccount resolveUser(String tenantId, AuthenticationResult result) {
        TenantId tenant = TenantId.of(tenantId);

        if (result.externalIdentity() != null && result.externalIdentity().getProvider() != AuthProviderType.LOCAL) {
            return userRepository
                    .findByExternalIdentity(tenant, result.externalIdentity())
                    .orElseThrow(() -> new IllegalStateException("User not provisioned for external identity"));
        }

        return userRepository
                .findByUsername(tenant, result.username())
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}