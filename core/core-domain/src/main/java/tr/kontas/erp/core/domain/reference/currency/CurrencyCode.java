package tr.kontas.erp.core.domain.reference.currency;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Identifier;

@Getter
public class CurrencyCode extends Identifier {
    public CurrencyCode(String value) {
        if (value == null || value.length() != 3) {
            throw new IllegalArgumentException("Currency code must be 3 characters (ISO)");
        }
        super(value);
    }

    @Override
    public String getValue() {
        return String.valueOf(super.getValue()).toUpperCase();
    }
}
