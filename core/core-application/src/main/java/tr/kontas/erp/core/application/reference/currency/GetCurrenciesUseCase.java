package tr.kontas.erp.core.application.reference.currency;

import tr.kontas.erp.core.domain.reference.currency.Currency;

import java.util.List;

public interface GetCurrenciesUseCase {
    List<Currency> execute();
}
