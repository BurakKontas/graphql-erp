package tr.kontas.erp.core.domain.reference.tax;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Identifier;

@Getter
public class TaxCode extends Identifier {
    public TaxCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tax code cannot be blank");
        }
        super(value);
    }

    @Override
    public String getValue() {
        return String.valueOf(super.getValue()).toUpperCase();
    }
}
