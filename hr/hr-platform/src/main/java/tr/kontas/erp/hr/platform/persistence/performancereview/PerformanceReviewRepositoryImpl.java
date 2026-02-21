package tr.kontas.erp.hr.platform.persistence.performancereview;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.performancereview.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PerformanceReviewRepositoryImpl implements PerformanceReviewRepository {

    private final JpaPerformanceReviewRepository jpaRepository;

    @Override
    public void save(PerformanceReview entity) {
        jpaRepository.save(PerformanceReviewMapper.toEntity(entity));
    }

    @Override
    public Optional<PerformanceReview> findById(PerformanceReviewId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(PerformanceReviewMapper::toDomain);
    }

    @Override
    public List<PerformanceReview> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(PerformanceReviewMapper::toDomain).toList();
    }

    @Override
    public List<PerformanceReview> findByIds(List<PerformanceReviewId> ids) {
        List<UUID> uuids = ids.stream().map(PerformanceReviewId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(PerformanceReviewMapper::toDomain).toList();
    }
}
