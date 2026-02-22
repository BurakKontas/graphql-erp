package tr.kontas.erp.notification.platform.persistence.preference;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.NotificationChannel;
import tr.kontas.erp.notification.domain.preference.NotificationPreference;
import tr.kontas.erp.notification.domain.preference.NotificationPreferenceId;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class NotificationPreferenceMapper {

    public static NotificationPreferenceJpaEntity toEntity(NotificationPreference domain) {
        NotificationPreferenceJpaEntity entity = new NotificationPreferenceJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setUserId(domain.getUserId());
        entity.setNotificationKey(domain.getNotificationKey());
        entity.setDisabledChannels(channelsToString(domain.getDisabledChannels()));
        entity.setPreferredLocale(domain.getPreferredLocale());
        return entity;
    }

    public static NotificationPreference toDomain(NotificationPreferenceJpaEntity entity) {
        return new NotificationPreference(
                NotificationPreferenceId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                entity.getUserId(),
                entity.getNotificationKey(),
                stringToChannels(entity.getDisabledChannels()),
                entity.getPreferredLocale()
        );
    }

    private static String channelsToString(Set<NotificationChannel> channels) {
        if (channels == null || channels.isEmpty()) return null;
        return channels.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    private static Set<NotificationChannel> stringToChannels(String value) {
        if (value == null || value.isBlank()) return new HashSet<>();
        return Arrays.stream(value.split(","))
                .map(NotificationChannel::valueOf)
                .collect(Collectors.toSet());
    }
}

