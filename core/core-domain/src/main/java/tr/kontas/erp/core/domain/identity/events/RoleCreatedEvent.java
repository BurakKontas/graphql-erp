package tr.kontas.erp.core.domain.identity.events;

import lombok.Getter;
import tr.kontas.erp.core.domain.identity.valueobjects.RoleId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

@Getter
public class RoleCreatedEvent extends DomainEvent {
    private final RoleId roleId;

    public RoleCreatedEvent(RoleId roleId) {
        this.roleId = roleId;
    }
}