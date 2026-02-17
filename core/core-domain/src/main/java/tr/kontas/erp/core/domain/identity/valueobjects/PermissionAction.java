package tr.kontas.erp.core.domain.identity.valueobjects;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class PermissionAction extends ValueObject {
    private final String value;

    public PermissionAction(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Permission action cannot be empty");
        }
        this.value = value;
    }

}
