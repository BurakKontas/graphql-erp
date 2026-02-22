package tr.kontas.erp.notification.application.preference;

import java.util.Set;

public record UpdateNotificationPreferenceCommand(
        String preferenceId,
        Set<String> disabledChannels,
        String preferredLocale
) {
}

