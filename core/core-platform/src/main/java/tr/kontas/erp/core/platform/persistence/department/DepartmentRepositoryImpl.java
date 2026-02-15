package tr.kontas.erp.core.platform.persistence.department;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.department.Department;
import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.domain.department.DepartmentRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final JpaDepartmentRepository jpaRepository;

    @Override
    public void save(Department department) {
        jpaRepository.save(DepartmentMapper.toEntity(department));
    }

    @Override
    public Optional<Department> findById(DepartmentId id) {
        return jpaRepository.findById(id.asUUID())
                .map(DepartmentMapper::toDomain);
    }

    @Override
    public Set<Department> findByTenantId(TenantId tenantId) {
        return jpaRepository.findByTenantId(tenantId.asUUID())
                .stream()
                .map(DepartmentMapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Department> findByCompanyId(CompanyId companyId) {
        return jpaRepository.findByCompanyId(companyId.asUUID(), Pageable.unpaged())
                .stream()
                .map(DepartmentMapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Department> findDepartmentsByIds(List<DepartmentId> ids) {
        if (ids == null || ids.isEmpty()) return List.of();

        List<UUID> uuidList = ids.stream()
                .map(DepartmentId::asUUID)
                .toList();

        return jpaRepository.findByIdIn(uuidList).stream()
                .map(DepartmentMapper::toDomain)
                .toList();
    }

    @Override
    public Set<Department> findDepartmentsByCompanyIds(List<CompanyId> ids) {
        if (ids == null || ids.isEmpty()) return Set.of();

        List<UUID> uuidList = ids.stream()
                .map(CompanyId::asUUID)
                .toList();

        return jpaRepository.findByCompanyIdIn(uuidList).stream()
                .map(DepartmentMapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean existsById(DepartmentId id) {
        return jpaRepository.countById(id.asUUID()) > 0;
    }
}
