package tr.kontas.erp.core.domain.identity.repositories;

import tr.kontas.erp.core.domain.identity.Permission;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionId;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionKey;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository {
    Optional<Permission> findById(PermissionId id);
    Optional<Permission> findByKey(PermissionKey key);
    List<Permission> findAllByIds(List<PermissionId> ids);
    List<Permission> findAll();
    void save(Permission permission);
}