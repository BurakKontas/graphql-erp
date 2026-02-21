package tr.kontas.erp.purchase.application.vendorinvoice;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoice;

import java.util.List;

public interface GetVendorInvoicesByCompanyUseCase {
    List<VendorInvoice> execute(CompanyId companyId);
}

