package tr.kontas.erp.core.domain.reference.currency;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Entity;

@Getter
public class Currency extends Entity<CurrencyCode> {

    private final String name;
    private final String symbol;
    private final int fractionDigits; // 2 for USD, 0 for JPY
    private boolean active;

    public Currency(CurrencyCode code, String name, String symbol, int fractionDigits) {
        super(code);

        if (fractionDigits < 0 || fractionDigits > 4) {
            throw new IllegalArgumentException("Invalid fraction digits");
        }

        this.name = name;
        this.symbol = symbol;
        this.fractionDigits = fractionDigits;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}