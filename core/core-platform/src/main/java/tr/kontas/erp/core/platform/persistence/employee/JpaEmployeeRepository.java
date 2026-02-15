package tr.kontas.erp.core.platform.persistence.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaEmployeeRepository extends JpaRepository<EmployeeJpaEntity, UUID> {
    List<EmployeeJpaEntity> findByDepartmentId(UUID departmentId);

    List<EmployeeJpaEntity> findByDepartmentIdIn(List<UUID> ids);
}
