package tr.kontas.erp.sales.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderNumber;

public interface SalesOrderNumberGeneratorPort {
    SalesOrderNumber generate(TenantId tenantId, CompanyId companyId, int year);
}
