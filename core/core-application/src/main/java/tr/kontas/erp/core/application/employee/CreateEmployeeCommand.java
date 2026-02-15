package tr.kontas.erp.core.application.employee;

import tr.kontas.erp.core.domain.department.DepartmentId;

public record CreateEmployeeCommand(DepartmentId departmentId, String name) {
}
