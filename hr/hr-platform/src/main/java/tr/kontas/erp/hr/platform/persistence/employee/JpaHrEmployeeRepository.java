package tr.kontas.erp.hr.platform.persistence.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaHrEmployeeRepository extends JpaRepository<HrEmployeeJpaEntity, UUID> {
    List<HrEmployeeJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<HrEmployeeJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
