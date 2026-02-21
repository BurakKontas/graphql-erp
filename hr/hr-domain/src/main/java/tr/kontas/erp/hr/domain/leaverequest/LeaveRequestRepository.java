package tr.kontas.erp.hr.domain.leaverequest;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository {
    void save(LeaveRequest entity);
    Optional<LeaveRequest> findById(LeaveRequestId id, TenantId tenantId);
    List<LeaveRequest> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<LeaveRequest> findByIds(List<LeaveRequestId> ids);
}
