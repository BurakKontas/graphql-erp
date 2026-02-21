package tr.kontas.erp.core.domain.employee;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class EmployeeCreatedEvent extends DomainEvent {
    private UUID employeeId;

    public EmployeeCreatedEvent(EmployeeId id) {
        this.employeeId = id.asUUID();
    }

    @Override
    public UUID getAggregateId() {
        return employeeId;
    }

    @Override
    public String getAggregateType() {
        return "Employee";
    }
}