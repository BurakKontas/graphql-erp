package tr.kontas.erp.notification.application.preference;

import tr.kontas.erp.notification.domain.preference.NotificationPreference;

import java.util.List;

public interface GetNotificationPreferencesByUserUseCase {
    List<NotificationPreference> execute(String userId);
}

