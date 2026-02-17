package tr.kontas.erp.core.domain.identity.valueobjects;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class UserName extends ValueObject {
    private final String value;

    public UserName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        this.value = value;
    }

}
