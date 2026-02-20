package tr.kontas.erp.core.application.reference.currency;

import tr.kontas.erp.core.domain.reference.currency.CurrencyCode;

public interface CreateCurrencyUseCase {
    CurrencyCode execute(CreateCurrencyCommand command);
}
