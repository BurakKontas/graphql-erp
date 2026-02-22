package tr.kontas.erp.notification.domain.tenantconfig;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class TenantNotificationConfigId extends Identifier {

    private TenantNotificationConfigId(UUID value) {
        super(value);
    }

    public static TenantNotificationConfigId newId() {
        return new TenantNotificationConfigId(UUID.randomUUID());
    }

    public static TenantNotificationConfigId of(UUID value) {
        return new TenantNotificationConfigId(value);
    }

    public static TenantNotificationConfigId of(String value) {
        return new TenantNotificationConfigId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

