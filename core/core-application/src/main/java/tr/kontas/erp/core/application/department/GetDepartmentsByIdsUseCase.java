package tr.kontas.erp.core.application.department;

import tr.kontas.erp.core.domain.department.Department;
import tr.kontas.erp.core.domain.department.DepartmentId;

import java.util.List;

public interface GetDepartmentsByIdsUseCase {
    List<Department> execute(List<DepartmentId> ids);
}
