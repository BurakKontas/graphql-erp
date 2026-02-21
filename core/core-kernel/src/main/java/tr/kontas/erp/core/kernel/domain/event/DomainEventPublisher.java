package tr.kontas.erp.core.kernel.domain.event;

import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.List;

public interface DomainEventPublisher {
    void publish(DomainEvent event);

    void publishAll(List<DomainEvent> events);
}
