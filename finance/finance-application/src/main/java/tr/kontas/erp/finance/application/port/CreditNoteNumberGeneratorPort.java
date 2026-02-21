package tr.kontas.erp.finance.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public interface CreditNoteNumberGeneratorPort {
    String generate(TenantId tenantId, CompanyId companyId, int year);
}

