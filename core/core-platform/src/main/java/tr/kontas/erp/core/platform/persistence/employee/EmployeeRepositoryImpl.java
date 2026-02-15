package tr.kontas.erp.core.platform.persistence.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.domain.employee.Employee;
import tr.kontas.erp.core.domain.employee.EmployeeId;
import tr.kontas.erp.core.domain.employee.EmployeeRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final JpaEmployeeRepository jpaRepository;

    @Override
    public void save(Employee employee) {
        jpaRepository.save(EmployeeMapper.toEntity(employee));
    }

    @Override
    public Optional<Employee> findById(EmployeeId id) {
        return jpaRepository.findById(id.asUUID())
                .map(EmployeeMapper::toDomain);
    }

    @Override
    public Set<Employee> findByDepartmentId(DepartmentId departmentId) {
        return jpaRepository.findByDepartmentId(departmentId.asUUID())
                .stream()
                .map(EmployeeMapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Employee> findByDepartmentIds(List<DepartmentId> ids) {
        if (ids == null || ids.isEmpty()) return Set.of();

        List<UUID> uuidList = ids.stream()
                .map(DepartmentId::asUUID)
                .toList();

        return jpaRepository.findByDepartmentIdIn(uuidList)
                .stream()
                .map(EmployeeMapper::toDomain)
                .collect(Collectors.toSet());
    }
}
