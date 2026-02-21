package tr.kontas.erp.core.domain.department;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class DepartmentCreatedEvent extends DomainEvent {
    private UUID departmentId;

    public DepartmentCreatedEvent(DepartmentId id) {
        this.departmentId = id.asUUID();
    }

    @Override
    public UUID getAggregateId() {
        return departmentId;
    }

    @Override
    public String getAggregateType() {
        return "Department";
    }
}