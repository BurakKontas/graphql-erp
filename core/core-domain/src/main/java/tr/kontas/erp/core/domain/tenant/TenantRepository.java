package tr.kontas.erp.core.domain.tenant;

import tr.kontas.erp.core.kernel.domain.repository.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Optional;

public interface TenantRepository extends Repository<Tenant, TenantId> {
    Optional<Tenant> findById(TenantId id);
    void save(Tenant tenant);
    Optional<TenantId> findIdByCode(TenantCode code);
}