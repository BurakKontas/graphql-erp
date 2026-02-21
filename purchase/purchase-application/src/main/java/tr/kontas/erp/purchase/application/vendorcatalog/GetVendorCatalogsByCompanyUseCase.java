package tr.kontas.erp.purchase.application.vendorcatalog;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.purchase.domain.vendorcatalog.VendorCatalog;

import java.util.List;

public interface GetVendorCatalogsByCompanyUseCase {
    List<VendorCatalog> execute(CompanyId companyId);
}

