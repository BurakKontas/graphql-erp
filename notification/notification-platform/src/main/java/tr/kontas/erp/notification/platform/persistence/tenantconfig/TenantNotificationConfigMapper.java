package tr.kontas.erp.notification.platform.persistence.tenantconfig;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.NotificationChannel;
import tr.kontas.erp.notification.domain.tenantconfig.TenantNotificationConfig;
import tr.kontas.erp.notification.domain.tenantconfig.TenantNotificationConfigId;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TenantNotificationConfigMapper {

    public static TenantNotificationConfigJpaEntity toEntity(TenantNotificationConfig domain) {
        TenantNotificationConfigJpaEntity entity = new TenantNotificationConfigJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setAllNotificationsEnabled(domain.isAllNotificationsEnabled());
        entity.setDisabledChannels(setToString(domain.getDisabledChannels()));
        entity.setDisabledKeys(stringSetToString(domain.getDisabledKeys()));
        entity.setDisabledReason(domain.getDisabledReason());
        entity.setDisabledBy(domain.getDisabledBy());
        entity.setDisabledAt(domain.getDisabledAt());
        entity.setDisabledUntil(domain.getDisabledUntil());
        return entity;
    }

    public static TenantNotificationConfig toDomain(TenantNotificationConfigJpaEntity entity) {
        return new TenantNotificationConfig(
                TenantNotificationConfigId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                entity.isAllNotificationsEnabled(),
                stringToChannelSet(entity.getDisabledChannels()),
                stringToStringSet(entity.getDisabledKeys()),
                entity.getDisabledReason(),
                entity.getDisabledBy(),
                entity.getDisabledAt(),
                entity.getDisabledUntil()
        );
    }

    private static String setToString(Set<NotificationChannel> channels) {
        if (channels == null || channels.isEmpty()) return null;
        return channels.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    private static Set<NotificationChannel> stringToChannelSet(String value) {
        if (value == null || value.isBlank()) return new HashSet<>();
        return Arrays.stream(value.split(","))
                .map(NotificationChannel::valueOf)
                .collect(Collectors.toSet());
    }

    private static String stringSetToString(Set<String> set) {
        if (set == null || set.isEmpty()) return null;
        return String.join(",", set);
    }

    private static Set<String> stringToStringSet(String value) {
        if (value == null || value.isBlank()) return new HashSet<>();
        return new HashSet<>(Arrays.asList(value.split(",")));
    }
}

