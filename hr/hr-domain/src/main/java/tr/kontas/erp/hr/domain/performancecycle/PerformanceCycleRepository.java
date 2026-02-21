package tr.kontas.erp.hr.domain.performancecycle;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface PerformanceCycleRepository {
    void save(PerformanceCycle entity);
    Optional<PerformanceCycle> findById(PerformanceCycleId id, TenantId tenantId);
    List<PerformanceCycle> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<PerformanceCycle> findByIds(List<PerformanceCycleId> ids);
}
