package tr.kontas.erp.notification.domain.preference;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class NotificationPreferenceId extends Identifier {

    private NotificationPreferenceId(UUID value) {
        super(value);
    }

    public static NotificationPreferenceId newId() {
        return new NotificationPreferenceId(UUID.randomUUID());
    }

    public static NotificationPreferenceId of(UUID value) {
        return new NotificationPreferenceId(value);
    }

    public static NotificationPreferenceId of(String value) {
        return new NotificationPreferenceId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

