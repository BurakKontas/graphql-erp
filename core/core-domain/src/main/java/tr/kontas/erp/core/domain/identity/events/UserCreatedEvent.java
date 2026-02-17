package tr.kontas.erp.core.domain.identity.events;

import lombok.Getter;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

@Getter
public class UserCreatedEvent extends DomainEvent {
    private final UserId userId;

    public UserCreatedEvent(UserId userId) {
        this.userId = userId;
    }
}