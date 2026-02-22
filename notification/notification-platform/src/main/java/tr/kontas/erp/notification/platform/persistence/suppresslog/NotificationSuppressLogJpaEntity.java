package tr.kontas.erp.notification.platform.persistence.suppresslog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notification_suppress_log")
@Getter
@Setter
@NoArgsConstructor
public class NotificationSuppressLogJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "notification_key", nullable = false)
    private String notificationKey;

    @Column(name = "channel", nullable = false)
    private String channel;

    @Column(name = "recipient_user_id")
    private String recipientUserId;

    @Column(name = "suppress_reason", nullable = false)
    private String suppressReason;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;
}

