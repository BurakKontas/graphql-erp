package tr.kontas.erp.hr.application.employee;

import tr.kontas.erp.hr.domain.employee.Employee;
import tr.kontas.erp.hr.domain.employee.EmployeeId;

import java.util.List;

public interface GetEmployeesByIdsUseCase {
    List<Employee> execute(List<EmployeeId> ids);
}
