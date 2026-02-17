package tr.kontas.erp.core.domain.identity.valueobjects;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class PermissionId extends Identifier {

    private PermissionId(UUID value) {
        super(value);
    }

    public static PermissionId newId() {
        return new PermissionId(UUID.randomUUID());
    }

    public static PermissionId of(UUID value) {
        return new PermissionId(value);
    }

    public static PermissionId of(String value) {
        return new PermissionId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
