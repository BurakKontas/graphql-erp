package tr.kontas.erp.notification.application.config;

import tr.kontas.erp.notification.domain.config.NotificationConfigId;

public interface CreateNotificationConfigUseCase {
    NotificationConfigId execute(CreateNotificationConfigCommand command);
}

