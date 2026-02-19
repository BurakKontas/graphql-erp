package tr.kontas.erp.core.platform.persistence.department;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.department.Department;
import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.domain.department.DepartmentName;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.HashSet;
import java.util.Set;

public class DepartmentMapper {

    public static Department toDomain(DepartmentJpaEntity entity) {
        DepartmentId parentId = entity.getParentId() != null ? DepartmentId.of(entity.getParentId()) : null;

        Department domain = new Department(
                DepartmentId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                new DepartmentName(entity.getName()),
                CompanyId.of(entity.getCompanyId()),
                parentId,
                entity.isActive()
        );

        if (entity.getSubDepartments() != null) {
            Set<DepartmentId> subIds = new HashSet<>();
            entity.getSubDepartments().forEach(sub -> subIds.add(DepartmentId.of(sub)));
            subIds.forEach(domain::addSubDepartmentId);
        }

        return domain;
    }

    public static DepartmentJpaEntity toEntity(Department domain) {
        return DepartmentJpaEntity.builder()
                .id(domain.getId().asUUID())
                .tenantId(domain.getTenantId().asUUID())
                .name(domain.getName().getValue())
                .active(domain.isActive())
                .companyId(domain.getCompanyId().asUUID())
                .parentId(domain.getParentId() != null ? domain.getParentId().asUUID() : null)
                .build();
    }
}
