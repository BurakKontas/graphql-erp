package tr.kontas.erp.purchase.application.vendorcatalog;

import tr.kontas.erp.purchase.domain.vendorcatalog.VendorCatalog;
import tr.kontas.erp.purchase.domain.vendorcatalog.VendorCatalogId;

import java.util.List;

public interface GetVendorCatalogsByIdsUseCase {
    List<VendorCatalog> execute(List<VendorCatalogId> ids);
}

