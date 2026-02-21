package tr.kontas.erp.hr.domain.leavepolicy;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface LeavePolicyRepository {
    void save(LeavePolicy entity);
    Optional<LeavePolicy> findById(LeavePolicyId id, TenantId tenantId);
    List<LeavePolicy> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<LeavePolicy> findByIds(List<LeavePolicyId> ids);
}
