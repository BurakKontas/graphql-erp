package tr.kontas.erp.notification.platform.persistence.tenantconfig;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tenant_notification_configs")
@Getter
@Setter
@NoArgsConstructor
public class TenantNotificationConfigJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "all_notifications_enabled", nullable = false)
    private boolean allNotificationsEnabled;

    @Column(name = "disabled_channels")
    private String disabledChannels;

    @Column(name = "disabled_keys")
    private String disabledKeys;

    @Column(name = "disabled_reason")
    private String disabledReason;

    @Column(name = "disabled_by")
    private String disabledBy;

    @Column(name = "disabled_at")
    private Instant disabledAt;

    @Column(name = "disabled_until")
    private Instant disabledUntil;
}

