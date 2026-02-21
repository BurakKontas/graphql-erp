package tr.kontas.erp.hr.application.employee;

import tr.kontas.erp.hr.domain.employee.Employee;
import tr.kontas.erp.hr.domain.employee.EmployeeId;

public interface GetEmployeeByIdUseCase {
    Employee execute(EmployeeId id);
}
