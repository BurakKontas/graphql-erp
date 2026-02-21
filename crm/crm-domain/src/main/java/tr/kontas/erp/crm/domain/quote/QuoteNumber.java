package tr.kontas.erp.crm.domain.quote;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class QuoteNumber extends ValueObject {

    private final String value;

    public QuoteNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Quote number cannot be blank");
        }
        this.value = value;
    }
}
