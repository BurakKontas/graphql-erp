package tr.kontas.erp.core.domain.department;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class DepartmentName extends ValueObject {
    private final String value;

    public DepartmentName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        this.value = value;
    }

}
