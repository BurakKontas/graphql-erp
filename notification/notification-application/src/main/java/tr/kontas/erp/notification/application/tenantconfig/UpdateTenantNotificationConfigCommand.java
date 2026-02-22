package tr.kontas.erp.notification.application.tenantconfig;

import java.time.Instant;

public record UpdateTenantNotificationConfigCommand(
        boolean allNotificationsEnabled,
        String disabledReason,
        Instant disabledUntil
) {
}

