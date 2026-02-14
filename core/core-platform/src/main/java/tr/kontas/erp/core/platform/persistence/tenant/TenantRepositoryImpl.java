package tr.kontas.erp.core.platform.persistence.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.tenant.Tenant;
import tr.kontas.erp.core.domain.tenant.TenantCode;
import tr.kontas.erp.core.domain.tenant.TenantName;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TenantRepositoryImpl implements TenantRepository {

    private final JpaTenantRepository jpaRepository;

    @Override
    public void save(Tenant tenant) {
        jpaRepository.save(TenantMapper.toEntity(tenant));
    }

    @Override
    public Optional<TenantId> findIdByCode(TenantCode code) {
        return jpaRepository.findIdByCode(code.getValue())
                .map(TenantId::of);
    }

    @Override
    public Optional<Tenant> findById(TenantId id) {
        return jpaRepository.findById(id.asUUID())
                .map(TenantMapper::toDomain);
    }
}