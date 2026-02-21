package tr.kontas.erp.finance.application.salesinvoice;

import tr.kontas.erp.finance.domain.salesinvoice.SalesInvoice;
import tr.kontas.erp.finance.domain.salesinvoice.SalesInvoiceId;

public interface GetSalesInvoiceByIdUseCase {
    SalesInvoice execute(SalesInvoiceId id);
}

