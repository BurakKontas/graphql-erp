package tr.kontas.erp.core.platform.persistence.identity.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.identity.Role;
import tr.kontas.erp.core.domain.identity.repositories.RoleRepository;
import tr.kontas.erp.core.domain.identity.valueobjects.RoleId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final JpaRoleRepository jpaRepository;

    @Override
    public void save(Role role) {
        jpaRepository.save(RoleMapper.toEntity(role));
    }

    @Override
    public Optional<Role> findById(RoleId id) {
        return jpaRepository.findById(id.asUUID())
                .map(RoleMapper::toDomain);
    }

    @Override
    public Set<Role> findAllByIds(Set<RoleId> ids) {
        List<UUID> roleIds = ids.stream().map(RoleId::asUUID).toList();

        return jpaRepository.findAllById(roleIds)
                .stream()
                .map(RoleMapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Role> findByTenantId(TenantId tenantId) {
        return jpaRepository.findByTenantId(tenantId.asUUID())
                .stream()
                .map(RoleMapper::toDomain)
                .collect(Collectors.toList());
    }
}