package tr.kontas.erp.hr.domain.payrollrun;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class PayrollRunNumber extends ValueObject {

    private final String value;

    public PayrollRunNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PayrollRunNumber cannot be empty");
        }
        this.value = value;
    }
}
