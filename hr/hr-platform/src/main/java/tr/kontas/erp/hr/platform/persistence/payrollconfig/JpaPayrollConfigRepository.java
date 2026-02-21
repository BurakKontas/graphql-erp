package tr.kontas.erp.hr.platform.persistence.payrollconfig;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaPayrollConfigRepository extends JpaRepository<PayrollConfigJpaEntity, UUID> {
    List<PayrollConfigJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<PayrollConfigJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
