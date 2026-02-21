package tr.kontas.erp.hr.domain.leavebalance;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceRepository {
    void save(LeaveBalance entity);
    Optional<LeaveBalance> findById(LeaveBalanceId id, TenantId tenantId);
    List<LeaveBalance> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<LeaveBalance> findByIds(List<LeaveBalanceId> ids);
}
