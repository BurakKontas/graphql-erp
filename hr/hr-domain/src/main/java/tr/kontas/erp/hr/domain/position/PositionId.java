package tr.kontas.erp.hr.domain.position;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class PositionId extends Identifier {
    private PositionId(UUID value) {
        super(value);
    }

    public static PositionId newId() {
        return new PositionId(UUID.randomUUID());
    }

    public static PositionId of(UUID value) {
        return new PositionId(value);
    }

    public static PositionId of(String value) {
        return new PositionId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
