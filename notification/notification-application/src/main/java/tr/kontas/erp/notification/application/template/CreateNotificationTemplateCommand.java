package tr.kontas.erp.notification.application.template;

import tr.kontas.erp.notification.domain.NotificationChannel;

public record CreateNotificationTemplateCommand(
        String notificationKey,
        NotificationChannel channel,
        String locale,
        String subject,
        String bodyTemplate,
        boolean systemTemplate
) {
}

