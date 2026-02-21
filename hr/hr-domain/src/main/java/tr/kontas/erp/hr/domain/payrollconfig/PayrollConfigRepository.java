package tr.kontas.erp.hr.domain.payrollconfig;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface PayrollConfigRepository {
    void save(PayrollConfig entity);
    Optional<PayrollConfig> findById(PayrollConfigId id, TenantId tenantId);
    List<PayrollConfig> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<PayrollConfig> findByIds(List<PayrollConfigId> ids);
}
