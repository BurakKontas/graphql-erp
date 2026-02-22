package tr.kontas.erp.notification.application.template;

import tr.kontas.erp.notification.domain.template.NotificationTemplate;
import tr.kontas.erp.notification.domain.template.NotificationTemplateId;

public interface GetNotificationTemplateByIdUseCase {
    NotificationTemplate execute(NotificationTemplateId id);
}

