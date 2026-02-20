package tr.kontas.erp.core.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.reference.currency.CreateCurrencyCommand;
import tr.kontas.erp.core.application.reference.currency.CreateCurrencyUseCase;
import tr.kontas.erp.core.application.reference.currency.GetCurrenciesUseCase;
import tr.kontas.erp.core.application.reference.currency.GetCurrencyByCodeUseCase;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.currency.CurrencyCode;
import tr.kontas.erp.core.domain.reference.currency.CurrencyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService implements
        CreateCurrencyUseCase,
        GetCurrencyByCodeUseCase,
        GetCurrenciesUseCase {

    private final CurrencyRepository currencyRepository;

    @Override
    @Transactional
    public CurrencyCode execute(CreateCurrencyCommand command) {
        CurrencyCode code = new CurrencyCode(command.code());

        if (currencyRepository.findByCode(code).isPresent()) {
            throw new IllegalArgumentException("Currency with code " + command.code() + " already exists");
        }

        Currency currency = new Currency(
                code,
                command.name(),
                command.symbol(),
                command.fractionDigits()
        );

        currencyRepository.save(currency);

        return code;
    }

    @Override
    public Currency execute(CurrencyCode code) {
        return currencyRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Currency not found: " + code.getValue()));
    }

    @Override
    public List<Currency> execute() {
        return currencyRepository.findAllActive();
    }
}
