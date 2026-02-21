package tr.kontas.erp.hr.platform.persistence.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaEmployeeRepository extends JpaRepository<EmployeeJpaEntity, UUID> {
    List<EmployeeJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<EmployeeJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
