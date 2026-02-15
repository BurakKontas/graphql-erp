package tr.kontas.erp.core.application.employee;

import tr.kontas.erp.core.domain.employee.Employee;
import tr.kontas.erp.core.domain.employee.EmployeeId;

public interface GetEmployeeByIdUseCase {
    Employee execute(EmployeeId id);
}
