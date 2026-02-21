package tr.kontas.erp.hr.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.payrollrun.PayrollRunNumber;

public interface PayrollRunNumberGeneratorPort {
    PayrollRunNumber generate(TenantId tenantId, CompanyId companyId, int year);
}
