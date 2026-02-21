package tr.kontas.erp.hr.application.employee;

import tr.kontas.erp.hr.domain.employee.EmployeeId;

public interface CreateEmployeeUseCase {
    EmployeeId execute(CreateEmployeeCommand command);
}
