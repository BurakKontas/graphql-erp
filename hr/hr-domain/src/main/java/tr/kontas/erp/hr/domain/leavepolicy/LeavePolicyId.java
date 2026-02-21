package tr.kontas.erp.hr.domain.leavepolicy;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class LeavePolicyId extends Identifier {
    private LeavePolicyId(UUID value) {
        super(value);
    }

    public static LeavePolicyId newId() {
        return new LeavePolicyId(UUID.randomUUID());
    }

    public static LeavePolicyId of(UUID value) {
        return new LeavePolicyId(value);
    }

    public static LeavePolicyId of(String value) {
        return new LeavePolicyId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
