package tr.kontas.erp.notification.platform.persistence.webhook;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "webhook_configs")
@Getter
@Setter
@NoArgsConstructor
public class WebhookConfigJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "target_url", nullable = false)
    private String targetUrl;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "event_types")
    private String eventTypes;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "consecutive_failures", nullable = false)
    private int consecutiveFailures;

    @Column(name = "last_success_at")
    private Instant lastSuccessAt;
}

