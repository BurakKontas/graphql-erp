package tr.kontas.erp.finance.application.salesinvoice;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.finance.domain.salesinvoice.SalesInvoice;
import java.util.List;

public interface GetSalesInvoicesByCompanyUseCase {
    List<SalesInvoice> execute(CompanyId companyId);
}

