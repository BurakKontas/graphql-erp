package tr.kontas.erp.notification.domain.template;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class NotificationTemplateId extends Identifier {

    private NotificationTemplateId(UUID value) {
        super(value);
    }

    public static NotificationTemplateId newId() {
        return new NotificationTemplateId(UUID.randomUUID());
    }

    public static NotificationTemplateId of(UUID value) {
        return new NotificationTemplateId(value);
    }

    public static NotificationTemplateId of(String value) {
        return new NotificationTemplateId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

