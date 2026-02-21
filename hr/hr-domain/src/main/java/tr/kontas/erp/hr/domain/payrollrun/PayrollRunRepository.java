package tr.kontas.erp.hr.domain.payrollrun;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface PayrollRunRepository {
    void save(PayrollRun entity);
    Optional<PayrollRun> findById(PayrollRunId id, TenantId tenantId);
    List<PayrollRun> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<PayrollRun> findByIds(List<PayrollRunId> ids);
}
