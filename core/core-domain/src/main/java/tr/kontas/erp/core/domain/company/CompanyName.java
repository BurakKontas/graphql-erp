package tr.kontas.erp.core.domain.company;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class CompanyName extends ValueObject {
    private final String value;

    public CompanyName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        this.value = value;
    }

}
