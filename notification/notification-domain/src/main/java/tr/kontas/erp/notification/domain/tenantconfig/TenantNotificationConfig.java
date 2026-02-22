package tr.kontas.erp.notification.domain.tenantconfig;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.NotificationChannel;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
public class TenantNotificationConfig extends AggregateRoot<TenantNotificationConfigId> {

    private final TenantId tenantId;
    private boolean allNotificationsEnabled;
    private Set<NotificationChannel> disabledChannels;
    private Set<String> disabledKeys;
    private String disabledReason;
    private String disabledBy;
    private Instant disabledAt;
    private Instant disabledUntil;

    public TenantNotificationConfig(
            TenantNotificationConfigId id,
            TenantId tenantId,
            boolean allNotificationsEnabled,
            Set<NotificationChannel> disabledChannels,
            Set<String> disabledKeys,
            String disabledReason,
            String disabledBy,
            Instant disabledAt,
            Instant disabledUntil) {
        super(id);
        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        this.tenantId = tenantId;
        this.allNotificationsEnabled = allNotificationsEnabled;
        this.disabledChannels = disabledChannels != null ? new HashSet<>(disabledChannels) : new HashSet<>();
        this.disabledKeys = disabledKeys != null ? new HashSet<>(disabledKeys) : new HashSet<>();
        this.disabledReason = disabledReason;
        this.disabledBy = disabledBy;
        this.disabledAt = disabledAt;
        this.disabledUntil = disabledUntil;
    }

    public static TenantNotificationConfig create(TenantNotificationConfigId id, TenantId tenantId) {
        return new TenantNotificationConfig(id, tenantId, true, new HashSet<>(), new HashSet<>(), null, null, null, null);
    }

    public void disableAll(String reason, String disabledBy, Instant disabledUntil) {
        this.allNotificationsEnabled = false;
        this.disabledReason = reason;
        this.disabledBy = disabledBy;
        this.disabledAt = Instant.now();
        this.disabledUntil = disabledUntil;
    }

    public void enableAll(String enabledBy) {
        this.allNotificationsEnabled = true;
        this.disabledReason = null;
        this.disabledBy = enabledBy;
        this.disabledAt = null;
        this.disabledUntil = null;
    }

    public void disableChannel(NotificationChannel channel) {
        this.disabledChannels.add(channel);
    }

    public void enableChannel(NotificationChannel channel) {
        this.disabledChannels.remove(channel);
    }

    public void disableKey(String notificationKey) {
        this.disabledKeys.add(notificationKey);
    }

    public void enableKey(String notificationKey) {
        this.disabledKeys.remove(notificationKey);
    }

    public boolean isEnabled(NotificationChannel channel, String notificationKey) {
        if (!allNotificationsEnabled) return false;
        if (disabledChannels.contains(channel)) return false;
        if (disabledKeys.contains(notificationKey)) return false;
        return true;
    }
}

