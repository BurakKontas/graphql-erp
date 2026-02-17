package tr.kontas.erp.core.platform.persistence.identity.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.identity.Permission;
import tr.kontas.erp.core.domain.identity.repositories.PermissionRepository;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionId;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionKey;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryImpl implements PermissionRepository {

    private final JpaPermissionRepository jpaRepository;

    @Override
    public Optional<Permission> findById(PermissionId id) {
        return jpaRepository.findById(id.asUUID())
                .map(PermissionMapper::toDomain);
    }

    @Override
    public Optional<Permission> findByKey(PermissionKey key) {
        return jpaRepository
                .findByModuleAndAction(
                        key.getModule().getValue(),
                        key.getAction().getValue()
                )
                .map(PermissionMapper::toDomain);
    }

    @Override
    public List<Permission> findAllByIds(List<PermissionId> ids) {
        List<UUID> permissionIds = ids.stream().map(PermissionId::asUUID).toList();
        return jpaRepository.findByIdIn(permissionIds)
                .stream()
                .map(PermissionMapper::toDomain)
                .toList();
    }

    @Override
    public void save(Permission permission) {
        jpaRepository.save(PermissionMapper.toEntity(permission));
    }
}
