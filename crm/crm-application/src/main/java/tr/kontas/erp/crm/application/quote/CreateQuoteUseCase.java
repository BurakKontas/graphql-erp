package tr.kontas.erp.crm.application.quote;

import tr.kontas.erp.crm.domain.quote.QuoteId;

public interface CreateQuoteUseCase {
    QuoteId execute(CreateQuoteCommand command);
}

