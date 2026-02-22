package tr.kontas.erp.notification.application.config;

import tr.kontas.erp.notification.domain.config.NotificationConfig;
import tr.kontas.erp.notification.domain.config.NotificationConfigId;

public interface GetNotificationConfigByIdUseCase {
    NotificationConfig execute(NotificationConfigId id);
}

