package tr.kontas.erp.core.application.department;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.department.DepartmentId;

public record CreateDepartmentCommand(DepartmentId parentId, CompanyId companyId, String name) {
}