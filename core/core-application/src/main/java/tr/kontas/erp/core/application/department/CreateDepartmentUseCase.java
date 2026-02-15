package tr.kontas.erp.core.application.department;

import tr.kontas.erp.core.domain.department.DepartmentId;

public interface CreateDepartmentUseCase {
    DepartmentId execute(CreateDepartmentCommand command);
}
