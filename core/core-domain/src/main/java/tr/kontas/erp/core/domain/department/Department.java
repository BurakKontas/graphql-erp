package tr.kontas.erp.core.domain.department;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Department extends AggregateRoot<DepartmentId> {

    private final TenantId tenantId;
    private final DepartmentName name;
    private final Set<DepartmentId> subDepartments = new HashSet<>();
    private final DepartmentId parentId;
    private final CompanyId companyId;
    private boolean active;

    public Department(DepartmentId id, TenantId tenantId, DepartmentName name, CompanyId companyId, DepartmentId parentId) {
        super(id);
        this.tenantId = tenantId;
        this.name = name;
        this.parentId = parentId;
        this.companyId = companyId;
        this.active = true;

        registerEvent(new DepartmentCreatedEvent(id));
    }

    public Department(DepartmentId id, TenantId tenantId, DepartmentName name, CompanyId companyId, DepartmentId parentId, boolean active) {
        super(id);
        this.tenantId = tenantId;
        this.name = name;
        this.parentId = parentId;
        this.companyId = companyId;
        this.active = active;
    }

    public void addSubDepartmentId(DepartmentId subId) {
        subDepartments.add(subId);
    }

    public void deactivate() {
        this.active = false;
    }
}
