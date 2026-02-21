package tr.kontas.erp.hr.domain.employee;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class EmployeeNumber extends ValueObject {

    private final String value;

    public EmployeeNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("EmployeeNumber cannot be empty");
        }
        this.value = value;
    }
}
