package tr.kontas.erp.finance.application.salesinvoice;

import tr.kontas.erp.finance.domain.salesinvoice.SalesInvoiceId;

public interface CreateSalesInvoiceUseCase {
    SalesInvoiceId execute(CreateSalesInvoiceCommand command);
}

