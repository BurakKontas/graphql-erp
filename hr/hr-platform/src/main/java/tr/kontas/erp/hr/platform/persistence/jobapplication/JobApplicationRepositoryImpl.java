package tr.kontas.erp.hr.platform.persistence.jobapplication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.jobapplication.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JobApplicationRepositoryImpl implements JobApplicationRepository {

    private final JpaJobApplicationRepository jpaRepository;

    @Override
    public void save(JobApplication entity) {
        jpaRepository.save(JobApplicationMapper.toEntity(entity));
    }

    @Override
    public Optional<JobApplication> findById(JobApplicationId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(JobApplicationMapper::toDomain);
    }

    @Override
    public List<JobApplication> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(JobApplicationMapper::toDomain).toList();
    }

    @Override
    public List<JobApplication> findByIds(List<JobApplicationId> ids) {
        List<UUID> uuids = ids.stream().map(JobApplicationId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(JobApplicationMapper::toDomain).toList();
    }
}
