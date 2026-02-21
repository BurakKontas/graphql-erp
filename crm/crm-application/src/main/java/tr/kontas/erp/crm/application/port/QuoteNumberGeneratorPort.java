package tr.kontas.erp.crm.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.quote.QuoteNumber;

public interface QuoteNumberGeneratorPort {
    QuoteNumber generate(TenantId tenantId, CompanyId companyId, int year);
}
