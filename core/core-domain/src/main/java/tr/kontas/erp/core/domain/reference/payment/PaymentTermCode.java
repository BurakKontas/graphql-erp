package tr.kontas.erp.core.domain.reference.payment;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Identifier;

@Getter
public class PaymentTermCode extends Identifier {
    public PaymentTermCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Payment term code cannot be blank");
        }
        super(value);
    }

    @Override
    public String getValue() {
        return String.valueOf(super.getValue()).toUpperCase();
    }
}
