package tr.kontas.erp.purchase.application.vendorcatalog;

import tr.kontas.erp.purchase.domain.vendorcatalog.VendorCatalog;
import tr.kontas.erp.purchase.domain.vendorcatalog.VendorCatalogId;

public interface GetVendorCatalogByIdUseCase {
    VendorCatalog execute(VendorCatalogId id);
}

