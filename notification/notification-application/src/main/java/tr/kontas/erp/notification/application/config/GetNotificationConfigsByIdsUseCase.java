package tr.kontas.erp.notification.application.config;

import tr.kontas.erp.notification.domain.config.NotificationConfig;
import tr.kontas.erp.notification.domain.config.NotificationConfigId;

import java.util.List;

public interface GetNotificationConfigsByIdsUseCase {
    List<NotificationConfig> execute(List<NotificationConfigId> ids);
}

