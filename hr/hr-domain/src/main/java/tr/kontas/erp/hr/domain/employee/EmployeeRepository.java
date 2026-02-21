package tr.kontas.erp.hr.domain.employee;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    void save(Employee entity);
    Optional<Employee> findById(EmployeeId id, TenantId tenantId);
    List<Employee> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Employee> findByIds(List<EmployeeId> ids);
}
