package tr.kontas.erp.notification.domain.config;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.DeliveryTiming;
import tr.kontas.erp.notification.domain.NotificationChannel;

import java.util.HashSet;
import java.util.Set;

@Getter
public class NotificationConfig extends AggregateRoot<NotificationConfigId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private String eventType;
    private String notificationKey;
    private String description;
    private Set<NotificationChannel> enabledChannels;
    private DeliveryTiming deliveryTiming;
    private String cronExpression;
    private Set<String> recipientRoles;
    private boolean userOverridable;
    private boolean active;

    public NotificationConfig(
            NotificationConfigId id,
            TenantId tenantId,
            CompanyId companyId,
            String eventType,
            String notificationKey,
            String description,
            Set<NotificationChannel> enabledChannels,
            DeliveryTiming deliveryTiming,
            String cronExpression,
            Set<String> recipientRoles,
            boolean userOverridable,
            boolean active) {
        super(id);
        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (eventType == null || eventType.isBlank()) throw new IllegalArgumentException("eventType cannot be blank");
        if (notificationKey == null || notificationKey.isBlank()) throw new IllegalArgumentException("notificationKey cannot be blank");
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.eventType = eventType;
        this.notificationKey = notificationKey;
        this.description = description;
        this.enabledChannels = enabledChannels != null ? new HashSet<>(enabledChannels) : new HashSet<>();
        this.deliveryTiming = deliveryTiming != null ? deliveryTiming : DeliveryTiming.IMMEDIATE;
        this.cronExpression = cronExpression;
        this.recipientRoles = recipientRoles != null ? new HashSet<>(recipientRoles) : new HashSet<>();
        this.userOverridable = userOverridable;
        this.active = active;
    }

    public void enableChannel(NotificationChannel channel) {
        this.enabledChannels.add(channel);
    }

    public void disableChannel(NotificationChannel channel) {
        this.enabledChannels.remove(channel);
    }

    public void updateTiming(DeliveryTiming timing, String cronExpression) {
        this.deliveryTiming = timing;
        this.cronExpression = cronExpression;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public boolean isChannelEnabled(NotificationChannel channel) {
        return active && enabledChannels.contains(channel);
    }
}

