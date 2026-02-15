package tr.kontas.erp.core.domain.department;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DepartmentRepository {
    void save(Department department);

    Optional<Department> findById(DepartmentId id);

    Set<Department> findByTenantId(TenantId tenantId);

    Set<Department> findByCompanyId(CompanyId companyId);

    List<Department> findDepartmentsByIds(List<DepartmentId> ids);

    Set<Department> findDepartmentsByCompanyIds(List<CompanyId> ids);

    boolean existsById(DepartmentId id);
}
