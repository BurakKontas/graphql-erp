package tr.kontas.erp.hr.platform.persistence.jobposting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.jobposting.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JobPostingRepositoryImpl implements JobPostingRepository {

    private final JpaJobPostingRepository jpaRepository;

    @Override
    public void save(JobPosting entity) {
        jpaRepository.save(JobPostingMapper.toEntity(entity));
    }

    @Override
    public Optional<JobPosting> findById(JobPostingId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(JobPostingMapper::toDomain);
    }

    @Override
    public List<JobPosting> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(JobPostingMapper::toDomain).toList();
    }

    @Override
    public List<JobPosting> findByIds(List<JobPostingId> ids) {
        List<UUID> uuids = ids.stream().map(JobPostingId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(JobPostingMapper::toDomain).toList();
    }
}
