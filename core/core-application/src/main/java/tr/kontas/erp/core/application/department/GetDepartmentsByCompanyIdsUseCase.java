package tr.kontas.erp.core.application.department;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.department.Department;

import java.util.List;
import java.util.Map;

public interface GetDepartmentsByCompanyIdsUseCase {
    Map<CompanyId, List<Department>> executeByCompanyIds(List<CompanyId> ids);
}
