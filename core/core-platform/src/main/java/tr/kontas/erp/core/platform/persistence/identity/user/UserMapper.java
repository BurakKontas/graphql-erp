package tr.kontas.erp.core.platform.persistence.identity.user;

import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.identity.valueobjects.*;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class UserMapper {

    public static UserAccount toDomain(UserJpaEntity entity) {
        Set<RoleId> roleIds = entity.getRoles()
                .stream()
                .map(RoleId::of)
                .collect(Collectors.toSet());

        return UserAccount.reconstitute(
                UserId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                new UserName(entity.getUsername()),
                entity.getPasswordHash() != null
                        ? new PasswordHash(entity.getPasswordHash())
                        : null,
                entity.getAuthProvider() != null
                        ? new ExternalIdentity(
                        AuthProviderType.valueOf(entity.getAuthProvider()),
                        entity.getExternalId()
                ) : null,
                entity.isActive(),
                entity.isLocked(),
                entity.getAuthVersion(),
                roleIds
        );
    }

    public static UserJpaEntity toEntity(UserAccount domain) {
        UserJpaEntity entity = new UserJpaEntity();

        entity.setId((UUID) domain.getId().getValue());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setUsername(domain.getUsername().getValue());
        entity.setAuthVersion(domain.getAuthVersion());

        if (domain.getPassword() != null) {
            entity.setPasswordHash(domain.getPassword().getValue());
        }

        if (domain.getExternalIdentity() != null) {
            entity.setAuthProvider(
                    domain.getExternalIdentity().getProvider().name()
            );
            entity.setExternalId(
                    domain.getExternalIdentity().getExternalId()
            );
        }

        entity.setActive(domain.isActive());
        entity.setLocked(domain.isLocked());

        Set<UUID> roleIds = domain.getRoles()
                .stream()
                .map(RoleId::asUUID)
                .collect(Collectors.toSet());

        entity.setRoles(roleIds);

        return entity;
    }
}
