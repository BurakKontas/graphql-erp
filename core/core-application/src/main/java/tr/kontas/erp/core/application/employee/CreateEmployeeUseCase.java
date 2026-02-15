package tr.kontas.erp.core.application.employee;

import tr.kontas.erp.core.domain.employee.EmployeeId;

public interface CreateEmployeeUseCase {
    EmployeeId execute(CreateEmployeeCommand command);
}

