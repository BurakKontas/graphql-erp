package tr.kontas.erp.core.domain.reference.tax;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.math.BigDecimal;

@Getter
public class TaxRate extends ValueObject {
    private final BigDecimal value;

    public TaxRate(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tax rate must be >= 0");
        }

        this.value = value;
    }

}
