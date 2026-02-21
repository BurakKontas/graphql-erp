package tr.kontas.erp.purchase.application.vendorcatalog;

import tr.kontas.erp.purchase.domain.vendorcatalog.VendorCatalogId;

public interface CreateVendorCatalogUseCase {
    VendorCatalogId execute(CreateVendorCatalogCommand command);
}

