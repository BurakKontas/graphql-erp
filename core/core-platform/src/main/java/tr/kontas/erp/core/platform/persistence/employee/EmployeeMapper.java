package tr.kontas.erp.core.platform.persistence.employee;

import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.domain.employee.Employee;
import tr.kontas.erp.core.domain.employee.EmployeeId;
import tr.kontas.erp.core.domain.employee.EmployeeName;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public class EmployeeMapper {

    public static Employee toDomain(EmployeeJpaEntity entity) {
        return new Employee(
                EmployeeId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                new EmployeeName(entity.getName()),
                DepartmentId.of(entity.getDepartmentId()),
                entity.isActive()
        );
    }

    public static EmployeeJpaEntity toEntity(Employee domain) {
        return EmployeeJpaEntity.builder()
                .id(domain.getId().asUUID())
                .tenantId(domain.getTenantId().asUUID())
                .name(domain.getName().getValue())
                .departmentId(domain.getDepartmentId().asUUID())
                .active(domain.isActive())
                .build();
    }
}
