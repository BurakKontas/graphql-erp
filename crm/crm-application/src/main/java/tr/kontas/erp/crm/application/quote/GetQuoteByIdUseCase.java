package tr.kontas.erp.crm.application.quote;

import tr.kontas.erp.crm.domain.quote.Quote;
import tr.kontas.erp.crm.domain.quote.QuoteId;

public interface GetQuoteByIdUseCase {
    Quote execute(QuoteId id);
}

