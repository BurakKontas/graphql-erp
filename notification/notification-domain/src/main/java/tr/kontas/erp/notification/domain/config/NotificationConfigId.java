package tr.kontas.erp.notification.domain.config;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class NotificationConfigId extends Identifier {

    private NotificationConfigId(UUID value) {
        super(value);
    }

    public static NotificationConfigId newId() {
        return new NotificationConfigId(UUID.randomUUID());
    }

    public static NotificationConfigId of(UUID value) {
        return new NotificationConfigId(value);
    }

    public static NotificationConfigId of(String value) {
        return new NotificationConfigId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

