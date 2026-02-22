package tr.kontas.erp.notification.application.template;

import tr.kontas.erp.notification.domain.template.NotificationTemplateId;

public interface CreateNotificationTemplateUseCase {
    NotificationTemplateId execute(CreateNotificationTemplateCommand command);
}

