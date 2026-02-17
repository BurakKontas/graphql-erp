package tr.kontas.erp.core.domain.identity.valueobjects;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class PermissionModule extends ValueObject {
    private final String value;

    public PermissionModule(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Permission module cannot be empty");
        }
        this.value = value;
    }

}
