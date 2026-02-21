package tr.kontas.erp.hr.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreatedEvent extends DomainEvent {

    private UUID employeeId;
    private UUID tenantId;
    private UUID companyId;
    private String employeeNumber;
    private String firstName;
    private String lastName;

    @Override
    public UUID getAggregateId() {
        return employeeId;
    }


    @Override
    public String getAggregateType() {
        return "Employee";
    }
}
