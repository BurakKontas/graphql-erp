package tr.kontas.erp.notification.platform.persistence.config;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.DeliveryTiming;
import tr.kontas.erp.notification.domain.NotificationChannel;
import tr.kontas.erp.notification.domain.config.NotificationConfig;
import tr.kontas.erp.notification.domain.config.NotificationConfigId;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class NotificationConfigMapper {

    public static NotificationConfigJpaEntity toEntity(NotificationConfig domain) {
        NotificationConfigJpaEntity entity = new NotificationConfigJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setEventType(domain.getEventType());
        entity.setNotificationKey(domain.getNotificationKey());
        entity.setDescription(domain.getDescription());
        entity.setEnabledChannels(channelsToString(domain.getEnabledChannels()));
        entity.setDeliveryTiming(domain.getDeliveryTiming().name());
        entity.setCronExpression(domain.getCronExpression());
        entity.setRecipientRoles(stringSetToString(domain.getRecipientRoles()));
        entity.setUserOverridable(domain.isUserOverridable());
        entity.setActive(domain.isActive());
        return entity;
    }

    public static NotificationConfig toDomain(NotificationConfigJpaEntity entity) {
        return new NotificationConfig(
                NotificationConfigId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                entity.getEventType(),
                entity.getNotificationKey(),
                entity.getDescription(),
                stringToChannels(entity.getEnabledChannels()),
                DeliveryTiming.valueOf(entity.getDeliveryTiming()),
                entity.getCronExpression(),
                stringToStringSet(entity.getRecipientRoles()),
                entity.isUserOverridable(),
                entity.isActive()
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

    private static String stringSetToString(Set<String> set) {
        if (set == null || set.isEmpty()) return null;
        return String.join(",", set);
    }

    private static Set<String> stringToStringSet(String value) {
        if (value == null || value.isBlank()) return new HashSet<>();
        return new HashSet<>(Arrays.asList(value.split(",")));
    }
}

