package tr.kontas.erp.hr.platform.persistence.payrollrun;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaPayrollRunRepository extends JpaRepository<PayrollRunJpaEntity, UUID> {
    List<PayrollRunJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<PayrollRunJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
