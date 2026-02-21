package tr.kontas.erp.hr.domain.attendance;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {
    void save(Attendance entity);
    Optional<Attendance> findById(AttendanceId id, TenantId tenantId);
    List<Attendance> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Attendance> findByIds(List<AttendanceId> ids);
}
