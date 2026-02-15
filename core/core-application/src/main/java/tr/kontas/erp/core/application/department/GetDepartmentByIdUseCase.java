package tr.kontas.erp.core.application.department;

import tr.kontas.erp.core.domain.department.Department;
import tr.kontas.erp.core.domain.department.DepartmentId;

public interface GetDepartmentByIdUseCase {
    Department execute(DepartmentId id);
}
