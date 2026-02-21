package tr.kontas.erp.hr.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.contract.ContractNumber;

public interface ContractNumberGeneratorPort {
    ContractNumber generate(TenantId tenantId, CompanyId companyId, int year);
}
