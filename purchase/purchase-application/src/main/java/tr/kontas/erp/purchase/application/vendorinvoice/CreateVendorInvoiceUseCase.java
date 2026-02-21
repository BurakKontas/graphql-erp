package tr.kontas.erp.purchase.application.vendorinvoice;

import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoiceId;

public interface CreateVendorInvoiceUseCase {
    VendorInvoiceId execute(CreateVendorInvoiceCommand command);
}

