package tr.kontas.erp.core.domain.identity.valueobjects;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class UserId extends Identifier {

    private UserId(UUID value) {
        super(value);
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(UUID value) {
        return new UserId(value);
    }

    public static UserId of(String value) {
        return new UserId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
