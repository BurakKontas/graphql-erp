package tr.kontas.erp.core.kernel.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
public abstract class DomainEvent {
    private final UUID eventId = UUID.randomUUID();
    private final Instant occurredOn = Instant.now();

    public abstract UUID getAggregateId();

    public abstract String getAggregateType();
}
