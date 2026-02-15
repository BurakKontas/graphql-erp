package tr.kontas.erp.core.domain.employee;

import tr.kontas.erp.core.domain.department.DepartmentId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeRepository {
    void save(Employee employee);

    Optional<Employee> findById(EmployeeId id);

    Set<Employee> findByDepartmentId(DepartmentId departmentId);

    Set<Employee> findByDepartmentIds(List<DepartmentId> ids);
}
