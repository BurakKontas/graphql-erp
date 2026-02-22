package tr.kontas.erp.hr.application.employee;

import tr.kontas.erp.hr.domain.employee.Employee;
import tr.kontas.erp.hr.domain.employee.EmployeeId;

public interface GetHrEmployeeByIdUseCase {
    Employee execute(EmployeeId id);
}
