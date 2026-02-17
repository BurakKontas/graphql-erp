package tr.kontas.erp.core.domain.identity.valueobjects;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class RoleId extends Identifier {

    private RoleId(UUID value) {
        super(value);
    }

    public static RoleId newId() {
        return new RoleId(UUID.randomUUID());
    }

    public static RoleId of(UUID value) {
        return new RoleId(value);
    }

    public static RoleId of(String value) {
        return new RoleId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
