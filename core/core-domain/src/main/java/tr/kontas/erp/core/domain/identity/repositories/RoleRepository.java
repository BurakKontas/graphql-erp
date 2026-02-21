package tr.kontas.erp.core.domain.identity.repositories;

import tr.kontas.erp.core.domain.identity.Role;
import tr.kontas.erp.core.domain.identity.valueobjects.RoleId;
import tr.kontas.erp.core.kernel.domain.repository.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends Repository<Role, RoleId> {
    Optional<Role> findById(RoleId id);
    Set<Role> findAllByIds(Set<RoleId> ids);
    List<Role> findByTenantId(TenantId tenantId);
    void save(Role role);
}
