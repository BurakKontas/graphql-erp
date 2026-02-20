package tr.kontas.erp.core.application.reference.currency;

public record CreateCurrencyCommand(
        String code,
        String name,
        String symbol,
        int fractionDigits
) {
}
