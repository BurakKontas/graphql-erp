package tr.kontas.erp.notification.application.preference;

import java.util.Set;

public record CreateNotificationPreferenceCommand(
        String userId,
        String notificationKey,
        Set<String> disabledChannels,
        String preferredLocale
) {
}

