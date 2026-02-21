package tr.kontas.erp.hr.platform.persistence.performancecycle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.performancecycle.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PerformanceCycleRepositoryImpl implements PerformanceCycleRepository {

    private final JpaPerformanceCycleRepository jpaRepository;

    @Override
    public void save(PerformanceCycle entity) {
        jpaRepository.save(PerformanceCycleMapper.toEntity(entity));
    }

    @Override
    public Optional<PerformanceCycle> findById(PerformanceCycleId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(PerformanceCycleMapper::toDomain);
    }

    @Override
    public List<PerformanceCycle> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(PerformanceCycleMapper::toDomain).toList();
    }

    @Override
    public List<PerformanceCycle> findByIds(List<PerformanceCycleId> ids) {
        List<UUID> uuids = ids.stream().map(PerformanceCycleId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(PerformanceCycleMapper::toDomain).toList();
    }
}
