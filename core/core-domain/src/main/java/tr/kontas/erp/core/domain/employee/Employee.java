package tr.kontas.erp.core.domain.employee;

import lombok.Getter;
import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Getter
public class Employee extends AggregateRoot<EmployeeId> {
    private final TenantId tenantId;
    private final EmployeeName name;
    private DepartmentId departmentId;
    private boolean active;

    public Employee(EmployeeId id, TenantId tenantId, EmployeeName name, DepartmentId departmentId) {
        super(id);
        this.tenantId = tenantId;
        this.name = name;
        this.departmentId = departmentId;
        this.active = true;

        registerEvent(new EmployeeCreatedEvent(id));
    }

    public Employee(EmployeeId id, TenantId tenantId, EmployeeName name, DepartmentId departmentId, boolean active) {
        super(id);
        this.tenantId = tenantId;
        this.name = name;
        this.departmentId = departmentId;
        this.active = active;
    }

    public void changeDepartment(DepartmentId newDepartmentId) {
        this.departmentId = newDepartmentId;
    }

    public void deactivate() {
        this.active = false;
    }
}
