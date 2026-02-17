package tr.kontas.erp.core.domain.identity;

import lombok.Getter;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionId;
import tr.kontas.erp.core.domain.identity.valueobjects.RoleId;
import tr.kontas.erp.core.domain.identity.valueobjects.RoleName;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Role extends AggregateRoot<RoleId> {
    private final TenantId tenantId;
    private final RoleName name;

    private final Set<PermissionId> permissions = new HashSet<>();

    private Role(
            RoleId id,
            TenantId tenantId,
            RoleName name
    ) {
        super(id);
        this.tenantId = tenantId;
        this.name = name;
    }

    public static Role create(
            RoleId id,
            TenantId tenantId,
            RoleName name
    ) {
        return new Role(id, tenantId, name);
    }

    public static Role reconstitute(
            RoleId id,
            TenantId tenantId,
            RoleName name,
            Set<PermissionId> permissions
    ) {
        Role role = new Role(id, tenantId, name);
        role.permissions.addAll(permissions);
        return role;
    }


    public void addPermission(PermissionId permissionId) {
        permissions.add(permissionId);
    }

    public void removePermission(PermissionId permissionId) {
        permissions.remove(permissionId);
    }
}
