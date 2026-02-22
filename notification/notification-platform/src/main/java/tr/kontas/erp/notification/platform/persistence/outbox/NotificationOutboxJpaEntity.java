package tr.kontas.erp.notification.platform.persistence.outbox;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notification_outbox")
@Getter
@Setter
@NoArgsConstructor
public class NotificationOutboxJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "notification_key", nullable = false)
    private String notificationKey;

    @Column(name = "channel", nullable = false)
    private String channel;

    @Column(name = "recipient_user_id")
    private String recipientUserId;

    @Column(name = "recipient_address")
    private String recipientAddress;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "scheduled_at")
    private Instant scheduledAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}

