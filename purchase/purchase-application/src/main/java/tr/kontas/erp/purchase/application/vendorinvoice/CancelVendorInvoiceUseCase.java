package tr.kontas.erp.purchase.application.vendorinvoice;

public interface CancelVendorInvoiceUseCase {
    void cancel(String invoiceId, String reason);
}

