package tr.kontas.erp.crm.domain.activity;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository {
    void save(Activity entity);
    Optional<Activity> findById(ActivityId id, TenantId tenantId);
    List<Activity> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Activity> findByIds(List<ActivityId> ids);
}

