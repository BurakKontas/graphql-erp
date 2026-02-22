package tr.kontas.erp.notification.application.preference;

import tr.kontas.erp.notification.domain.preference.NotificationPreferenceId;

public interface CreateNotificationPreferenceUseCase {
    NotificationPreferenceId execute(CreateNotificationPreferenceCommand command);
}

