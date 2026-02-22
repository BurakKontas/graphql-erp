package tr.kontas.erp.notification.platform.persistence.config;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "notification_configs")
@Getter
@Setter
@NoArgsConstructor
public class NotificationConfigJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "notification_key", nullable = false)
    private String notificationKey;

    @Column(name = "description")
    private String description;

    @Column(name = "enabled_channels")
    private String enabledChannels;

    @Column(name = "delivery_timing", nullable = false)
    private String deliveryTiming;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "recipient_roles")
    private String recipientRoles;

    @Column(name = "user_overridable", nullable = false)
    private boolean userOverridable;

    @Column(name = "active", nullable = false)
    private boolean active;
}

