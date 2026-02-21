package tr.kontas.erp.core.platform.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventPublisherImpl implements DomainEventPublisher {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final OutboxRepository outboxRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(DomainEvent event) {
        OutboxEntity entry = saveToOutbox(event);
        tryPublishImmediately(event, entry);
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        List<OutboxEntity> entries = events.stream()
                .map(this::saveToOutbox)
                .toList();

        for (int i = 0; i < events.size(); i++) {
            tryPublishImmediately(events.get(i), entries.get(i));
        }
    }

    private OutboxEntity saveToOutbox(DomainEvent event) {
        try {
            String payload = MAPPER.writeValueAsString(event);

            OutboxEntity entry = new OutboxEntity(
                    event.getEventId(),
                    event.getAggregateType(),
                    event.getAggregateId(),
                    event.getClass().getName(),
                    payload,
                    event.getOccurredOn()
            );

            return outboxRepository.save(entry);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize domain event to outbox: {}", event.getClass().getSimpleName(), e);
            throw new RuntimeException("Failed to serialize domain event", e);
        }
    }

    private void tryPublishImmediately(DomainEvent event, OutboxEntity entry) {
        try {
            applicationEventPublisher.publishEvent(event);

            entry.markPublished();
            outboxRepository.save(entry);
        } catch (Exception e) {
            log.warn("Immediate event publish failed (outbox job will retry): {}",
                    event.getClass().getSimpleName(), e);
        }
    }
}
