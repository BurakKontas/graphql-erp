package tr.kontas.erp.core.domain.identity.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class UserCreatedEvent extends DomainEvent {
    private UUID userId;

    public UserCreatedEvent(UserId userId) {
        this.userId = userId.asUUID();
    }

    @Override
    public UUID getAggregateId() {
        return userId;
    }

    @Override
    public String getAggregateType() {
        return "UserAccount";
    }
}