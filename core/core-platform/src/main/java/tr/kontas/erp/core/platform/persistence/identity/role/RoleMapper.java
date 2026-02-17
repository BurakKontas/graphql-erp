package tr.kontas.erp.core.platform.persistence.identity.role;

import tr.kontas.erp.core.domain.identity.Role;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionId;
import tr.kontas.erp.core.domain.identity.valueobjects.RoleId;
import tr.kontas.erp.core.domain.identity.valueobjects.RoleName;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class RoleMapper {
    public static Role toDomain(RoleJpaEntity entity) {

        Role role = Role.create(
                RoleId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                new RoleName(entity.getName())
        );

        Set<PermissionId> permissionIds = entity.getPermissions()
                .stream()
                .map(PermissionId::of)
                .collect(Collectors.toSet());

        permissionIds.forEach(role::addPermission);

        return role;
    }

    public static RoleJpaEntity toEntity(Role domain) {

        RoleJpaEntity entity = new RoleJpaEntity();
        entity.setId((UUID) domain.getId().getValue());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setName(domain.getName().getValue());

        Set<UUID> permissionIds = domain.getPermissions()
                .stream()
                .map(PermissionId::asUUID)
                .collect(Collectors.toSet());

        entity.setPermissions(permissionIds);

        return entity;
    }
}