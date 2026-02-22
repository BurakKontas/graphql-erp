package tr.kontas.erp.notification.application.inapp;

import java.time.Instant;

public record InAppNotificationResult(
        String id,
        String userId,
        String notificationKey,
        String title,
        String body,
        String actionUrl,
        boolean read,
        Instant readAt,
        Instant createdAt
) {
}

