package tr.kontas.erp.crm.platform.persistence.activity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.activity.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepository {

    private final JpaActivityRepository jpa;

    @Override
    public void save(Activity entity) {
        jpa.save(ActivityMapper.toEntity(entity));
    }

    @Override
    public Optional<Activity> findById(ActivityId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(ActivityMapper::toDomain);
    }

    @Override
    public List<Activity> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(ActivityMapper::toDomain).toList();
    }

    @Override
    public List<Activity> findByIds(List<ActivityId> ids) {
        return jpa.findByIdIn(ids.stream().map(ActivityId::asUUID).toList())
                .stream().map(ActivityMapper::toDomain).toList();
    }
}

