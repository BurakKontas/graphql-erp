package tr.kontas.erp.core.platform.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisherJob {

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEntity> pending = outboxRepository.findByPublishedFalseOrderByOccurredOnAsc();

        if (pending.isEmpty()) {
            return;
        }

        log.info("Publishing {} pending outbox events", pending.size());

        for (OutboxEntity entry : pending) {
            try {
                DomainEvent event = deserialize(entry);
                applicationEventPublisher.publishEvent(event);

                entry.markPublished();
                outboxRepository.save(entry);

                log.debug("Published outbox event: id={}, type={}", entry.getId(), entry.getEventType());
            } catch (Exception e) {
                log.error("Failed to publish outbox event: id={}, type={}",
                        entry.getId(), entry.getEventType(), e);
                break;
            }
        }
    }

    private DomainEvent deserialize(OutboxEntity entry) {
        try {
            Class<? extends DomainEvent> eventClass =
                    (Class<? extends DomainEvent>) Class.forName(entry.getEventType());
            return objectMapper.readValue(entry.getPayload(), eventClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Unknown event type: " + entry.getEventType() + " (id=" + entry.getId() + ")", e);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to deserialize event: " + entry.getEventType() + " (id=" + entry.getId() + ")", e);
        }
    }
}
