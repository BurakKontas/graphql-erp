package tr.kontas.erp.core.domain.idempotency;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class IdempotencyKey extends Identifier {

    private IdempotencyKey(UUID value) {
        super(value);
    }

    public static IdempotencyKey newId() {
        return new IdempotencyKey(UUID.randomUUID());
    }

    public static IdempotencyKey of(UUID value) {
        return new IdempotencyKey(value);
    }

    public static IdempotencyKey of(String value) {
        return new IdempotencyKey(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
