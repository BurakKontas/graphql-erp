package tr.kontas.erp.purchase.application.vendorinvoice;

import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoice;
import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoiceId;

import java.util.List;

public interface GetVendorInvoicesByIdsUseCase {
    List<VendorInvoice> execute(List<VendorInvoiceId> ids);
}

