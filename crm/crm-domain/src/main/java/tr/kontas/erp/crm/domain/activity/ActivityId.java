package tr.kontas.erp.crm.domain.activity;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class ActivityId extends Identifier {

    private ActivityId(UUID value) {
        super(value);
    }


    public static ActivityId newId() {
        return new ActivityId(UUID.randomUUID());
    }


    public static ActivityId of(UUID value) {
        return new ActivityId(value);
    }


    public static ActivityId of(String value) {
        return new ActivityId(UUID.fromString(value));
    }


    public UUID asUUID() {
        return (UUID) getValue();
    }
}

