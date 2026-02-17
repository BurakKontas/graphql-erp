package tr.kontas.erp.core.platform.service.identity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.application.identity.PasswordEncoder;
import tr.kontas.erp.core.application.identity.RegisterCommand;
import tr.kontas.erp.core.application.identity.RegisterUserUseCase;
import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.identity.repositories.UserRepository;
import tr.kontas.erp.core.domain.identity.valueobjects.PasswordHash;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.domain.identity.valueobjects.UserName;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserId execute(RegisterCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        UserName username = new UserName(command.username());

        AuthProviderType authMode = tenantRepository.findAuthModeById(tenantId)
                .orElse(AuthProviderType.LOCAL);

        if (authMode != AuthProviderType.LOCAL) {
            throw new IllegalStateException(
                    "Registration is only available for LOCAL authentication mode. " +
                            "Current tenant uses: " + authMode
            );
        }

        if (userRepository.findByUsername(tenantId, username).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + command.username());
        }

        String hashedPassword = passwordEncoder.hash(command.password());
        PasswordHash passwordHash = new PasswordHash(hashedPassword);

        UserAccount user = UserAccount.createLocal(tenantId, username, passwordHash);

        userRepository.save(user);

        return user.getId();
    }
}
