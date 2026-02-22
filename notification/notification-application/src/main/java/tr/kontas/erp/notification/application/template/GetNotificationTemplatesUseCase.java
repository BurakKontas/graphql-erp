package tr.kontas.erp.notification.application.template;

import tr.kontas.erp.notification.domain.template.NotificationTemplate;

import java.util.List;

public interface GetNotificationTemplatesUseCase {
    List<NotificationTemplate> execute();
}

