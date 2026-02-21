package tr.kontas.erp.core.domain.identity.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.identity.valueobjects.RoleId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class RoleCreatedEvent extends DomainEvent {
    private UUID roleId;

    public RoleCreatedEvent(RoleId roleId) {
        this.roleId = roleId.asUUID();
    }

    @Override
    public UUID getAggregateId() {
        return roleId;
    }

    @Override
    public String getAggregateType() {
        return "Role";
    }
}