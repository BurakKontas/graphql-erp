package tr.kontas.erp.core.application.employee;

import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.domain.employee.Employee;

import java.util.List;
import java.util.Map;

public interface GetEmployeesByDepartmentIdsUseCase {
    Map<DepartmentId, List<Employee>> execute(List<DepartmentId> ids);
}
