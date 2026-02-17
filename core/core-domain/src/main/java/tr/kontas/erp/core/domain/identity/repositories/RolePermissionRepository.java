package tr.kontas.erp.core.domain.identity.repositories;

import tr.kontas.erp.core.domain.identity.Permission;
import tr.kontas.erp.core.domain.identity.valueobjects.RoleId;

import java.util.Set;

public interface RolePermissionRepository {
    Set<Permission> findPermissionsByRole(RoleId roleId);

    Set<Permission> findPermissionsByRoles(Set<RoleId> roleIds);
}
