package tr.kontas.erp.notification.platform.persistence.preference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@NoArgsConstructor
public class NotificationPreferenceJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "notification_key", nullable = false)
    private String notificationKey;

    @Column(name = "disabled_channels")
    private String disabledChannels;

    @Column(name = "preferred_locale")
    private String preferredLocale;
}

