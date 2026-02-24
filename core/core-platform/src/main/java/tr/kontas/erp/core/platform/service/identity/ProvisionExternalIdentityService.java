package tr.kontas.erp.core.platform.service.identity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.application.identity.ProvisionExternalIdentityCommand;
import tr.kontas.erp.core.application.identity.ProvisionExternalIdentityResult;
import tr.kontas.erp.core.application.identity.ProvisionExternalIdentityUseCase;
import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.identity.repositories.UserRepository;
import tr.kontas.erp.core.domain.identity.valueobjects.ExternalIdentity;
import tr.kontas.erp.core.domain.identity.valueobjects.UserName;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProvisionExternalIdentityService implements ProvisionExternalIdentityUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProvisionExternalIdentityResult execute(ProvisionExternalIdentityCommand command) {
        TenantId tenant = TenantId.of(command.tenantId());

        AuthProviderType provider = AuthProviderType.valueOf(command.provider());
        ExternalIdentity ext = new ExternalIdentity(provider, command.externalId());

        // if external identity already exists, return its info
        Optional<UserAccount> byExt = userRepository.findByExternalIdentity(tenant, ext);
        if (byExt.isPresent()) {
            UserAccount user = byExt.get();
            return new ProvisionExternalIdentityResult(user.getId().asUUID().toString(), user.getUsername().getValue(), provider.name(), ext.getExternalId());
        }

        // if username provided, link external identity to that user
        boolean isUsernameValid = command.username() != null && !command.username().isBlank();
        if (isUsernameValid) {
            Optional<UserAccount> byUsername = userRepository.findByUsername(tenant, new UserName(command.username()));
            if (byUsername.isPresent()) {
                UserAccount user = byUsername.get();
                UserAccount updated = UserAccount.reconstitute(
                        user.getId(),
                        user.getTenantId(),
                        user.getUsername(),
                        null,
                        ext,
                        user.isActive(),
                        user.isLocked(),
                        user.getAuthVersion(),
                        user.getRoles()
                );
                userRepository.save(updated);
                return new ProvisionExternalIdentityResult(updated.getId().asUUID().toString(), updated.getUsername().getValue(), provider.name(), ext.getExternalId());
            }
        }

        // else create a new user with external identity
        UserName uname = isUsernameValid ? new UserName(command.username()) : null;
        UserAccount created = UserAccount.createExternal(tenant, uname, ext);
        userRepository.save(created);

        return new ProvisionExternalIdentityResult(created.getId().asUUID().toString(), created.getUsername().getValue(), provider.name(), ext.getExternalId());
    }
}
