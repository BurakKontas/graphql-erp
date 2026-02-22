package tr.kontas.erp.hr.application.employee;

import tr.kontas.erp.hr.domain.employee.Employee;
import tr.kontas.erp.hr.domain.employee.EmployeeId;

import java.util.List;

public interface GetHrEmployeesByIdsUseCase {
    List<Employee> execute(List<EmployeeId> ids);
}
