package tr.kontas.erp.core.platform.persistence.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.tenant.Tenant;
import tr.kontas.erp.core.domain.tenant.TenantCode;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TenantRepositoryImpl implements TenantRepository {

    private final JpaTenantRepository jpaRepository;

    @Override
    public void save(Tenant tenant) {
        if(jpaRepository.existsByCode(tenant.getCode().getValue())) {
            throw new IllegalArgumentException("Tenant code must be unique");
        }

        jpaRepository.save(TenantMapper.toEntity(tenant));
    }

    @Override
    public Optional<TenantId> findIdByCode(TenantCode code) {
        return jpaRepository.findIdByCode(code.getValue())
                .map(TenantId::of);
    }

    @Override
    public List<Tenant> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(TenantMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Tenant> findById(TenantId id) {
        return jpaRepository.findById(id.asUUID())
                .map(TenantMapper::toDomain);
    }

    @Override
    public Optional<AuthProviderType> findAuthModeById(TenantId id) {
        return jpaRepository.findAuthModeById(id.asUUID());
    }
}