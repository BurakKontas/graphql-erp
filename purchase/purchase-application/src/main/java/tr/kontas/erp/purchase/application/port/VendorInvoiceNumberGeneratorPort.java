package tr.kontas.erp.purchase.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoiceNumber;

public interface VendorInvoiceNumberGeneratorPort {
    VendorInvoiceNumber generate(TenantId tenantId, CompanyId companyId, int year);
}

