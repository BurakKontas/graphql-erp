package tr.kontas.erp.notification.domain.preference;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.NotificationChannel;

import java.util.HashSet;
import java.util.Set;

@Getter
public class NotificationPreference extends AggregateRoot<NotificationPreferenceId> {

    private final TenantId tenantId;
    private final String userId;
    private String notificationKey;
    private Set<NotificationChannel> disabledChannels;
    private String preferredLocale;

    public NotificationPreference(
            NotificationPreferenceId id,
            TenantId tenantId,
            String userId,
            String notificationKey,
            Set<NotificationChannel> disabledChannels,
            String preferredLocale) {
        super(id);
        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("userId cannot be blank");
        if (notificationKey == null || notificationKey.isBlank()) throw new IllegalArgumentException("notificationKey cannot be blank");
        this.tenantId = tenantId;
        this.userId = userId;
        this.notificationKey = notificationKey;
        this.disabledChannels = disabledChannels != null ? new HashSet<>(disabledChannels) : new HashSet<>();
        this.preferredLocale = preferredLocale;
    }

    public void disableChannel(NotificationChannel channel) {
        this.disabledChannels.add(channel);
    }

    public void enableChannel(NotificationChannel channel) {
        this.disabledChannels.remove(channel);
    }

    public void updateLocale(String locale) {
        this.preferredLocale = locale;
    }

    public boolean isChannelEnabled(NotificationChannel channel) {
        return !disabledChannels.contains(channel);
    }
}

