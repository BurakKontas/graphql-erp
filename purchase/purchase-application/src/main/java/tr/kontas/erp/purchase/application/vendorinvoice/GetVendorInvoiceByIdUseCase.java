package tr.kontas.erp.purchase.application.vendorinvoice;

import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoice;
import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoiceId;

public interface GetVendorInvoiceByIdUseCase {
    VendorInvoice execute(VendorInvoiceId id);
}

