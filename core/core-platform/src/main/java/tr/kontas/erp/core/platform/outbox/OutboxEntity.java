package tr.kontas.erp.core.platform.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "domain_outbox")
@Getter
@Setter
@NoArgsConstructor
public class OutboxEntity {

    @Id
    private UUID id;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "event_type", nullable = false, length = 500)
    private String eventType;

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "occurred_on", nullable = false)
    private Instant occurredOn;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "published_at")
    private Instant publishedAt;

    public OutboxEntity(UUID id, String aggregateType, UUID aggregateId,
                        String eventType, String payload, Instant occurredOn) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.occurredOn = occurredOn;
        this.published = false;
    }

    public void markPublished() {
        this.published = true;
        this.publishedAt = Instant.now();
    }
}
