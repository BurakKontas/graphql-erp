package tr.kontas.erp.core.application.reference.currency;

import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.currency.CurrencyCode;

public interface GetCurrencyByCodeUseCase {
    Currency execute(CurrencyCode code);
}
