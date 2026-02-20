package tr.kontas.erp.app.reference.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import tr.kontas.erp.app.reference.dtos.CreateCurrencyInput;
import tr.kontas.erp.app.reference.dtos.CurrencyPayload;
import tr.kontas.erp.core.application.reference.currency.CreateCurrencyCommand;
import tr.kontas.erp.core.application.reference.currency.CreateCurrencyUseCase;
import tr.kontas.erp.core.application.reference.currency.GetCurrenciesUseCase;
import tr.kontas.erp.core.application.reference.currency.GetCurrencyByCodeUseCase;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.currency.CurrencyCode;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class CurrencyGraphql {

    private final CreateCurrencyUseCase createCurrencyUseCase;
    private final GetCurrencyByCodeUseCase getCurrencyByCodeUseCase;
    private final GetCurrenciesUseCase getCurrenciesUseCase;

    public static CurrencyPayload toPayload(Currency currency) {
        return new CurrencyPayload(
                currency.getId().getValue(),
                currency.getName(),
                currency.getSymbol(),
                currency.getFractionDigits(),
                currency.isActive()
        );
    }

    @DgsMutation
    public CurrencyPayload createCurrency(@InputArgument("input") CreateCurrencyInput input) {
        CreateCurrencyCommand command = new CreateCurrencyCommand(
                input.getCode(),
                input.getName(),
                input.getSymbol(),
                input.getFractionDigits()
        );

        CurrencyCode code = createCurrencyUseCase.execute(command);

        return new CurrencyPayload(
                code.getValue(),
                input.getName(),
                input.getSymbol(),
                input.getFractionDigits(),
                true
        );
    }

    @DgsQuery
    public List<CurrencyPayload> currencies() {
        return getCurrenciesUseCase.execute()
                .stream()
                .map(CurrencyGraphql::toPayload)
                .toList();
    }

    @DgsQuery
    public CurrencyPayload currency(@InputArgument("code") String code) {
        CurrencyCode currencyCode = new CurrencyCode(code);
        return toPayload(getCurrencyByCodeUseCase.execute(currencyCode));
    }
}
