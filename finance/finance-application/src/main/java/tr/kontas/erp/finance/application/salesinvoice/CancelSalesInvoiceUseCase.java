package tr.kontas.erp.finance.application.salesinvoice;

public interface CancelSalesInvoiceUseCase {
    void execute(String invoiceId, String reason);
}

