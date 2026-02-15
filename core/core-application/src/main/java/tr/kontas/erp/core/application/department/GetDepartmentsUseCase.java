package tr.kontas.erp.core.application.department;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.department.Department;

import java.util.Set;

public interface GetDepartmentsUseCase {
    Set<Department> execute(CompanyId id);
}

