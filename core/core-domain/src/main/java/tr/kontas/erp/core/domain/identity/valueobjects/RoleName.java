package tr.kontas.erp.core.domain.identity.valueobjects;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class RoleName extends ValueObject {
    private final String value;

    public RoleName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be empty");
        }
        this.value = value;
    }

}
