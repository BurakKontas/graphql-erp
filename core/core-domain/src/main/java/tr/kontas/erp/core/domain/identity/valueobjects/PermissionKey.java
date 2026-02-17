package tr.kontas.erp.core.domain.identity.valueobjects;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class PermissionKey extends ValueObject {
    private final PermissionModule module;
    private final PermissionAction action;

    public PermissionKey(PermissionModule module, PermissionAction action) {
        if (module == null || action == null) {
            throw new IllegalArgumentException("PermissionKey fields cannot be null");
        }
        this.module = module;
        this.action = action;
    }

    @Override
    public String toString() {
        return module + ":" + action;
    }
}

