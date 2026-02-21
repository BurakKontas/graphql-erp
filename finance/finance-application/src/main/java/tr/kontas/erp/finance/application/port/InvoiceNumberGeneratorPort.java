package tr.kontas.erp.finance.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public interface InvoiceNumberGeneratorPort {
    String generate(TenantId tenantId, CompanyId companyId, int year);
}

