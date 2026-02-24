package tr.kontas.erp.core.platform.service.identity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.identity.*;
import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.identity.repositories.UserRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.domain.identity.valueobjects.ExternalIdentity;
import tr.kontas.erp.core.domain.identity.valueobjects.UserName;

import java.util.List;
import java.util.Optional;

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

        UserAccount user = resolveOrProvisionUser(command.tenantId(), result);

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

    private UserAccount resolveOrProvisionUser(String tenantId, AuthenticationResult result) {
        TenantId tenant = TenantId.of(tenantId);

        if (result.externalIdentity() != null && result.externalIdentity().getProvider() != AuthProviderType.LOCAL) {
            ExternalIdentity ext = result.externalIdentity();
            Optional<UserAccount> existing = userRepository.findByExternalIdentity(tenant, ext);
            if (existing.isPresent()) return existing.get();

            // provision a new user for this external identity
            UserAccount newUser = UserAccount.createExternal(tenant, result.username() != null ? result.username() : new UserName(ext.getExternalId()), ext);
            userRepository.save(newUser);
            return newUser;
        }

        return userRepository
                .findByUsername(tenant, result.username())
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}