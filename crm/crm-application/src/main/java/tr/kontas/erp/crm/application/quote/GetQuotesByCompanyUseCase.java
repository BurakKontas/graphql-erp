package tr.kontas.erp.crm.application.quote;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.crm.domain.quote.Quote;

import java.util.List;

public interface GetQuotesByCompanyUseCase {
    List<Quote> execute(CompanyId companyId);
}

