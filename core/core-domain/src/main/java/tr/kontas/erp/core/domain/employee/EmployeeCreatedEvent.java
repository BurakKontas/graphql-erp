package tr.kontas.erp.core.domain.employee;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

@Getter
@RequiredArgsConstructor
public class EmployeeCreatedEvent extends DomainEvent {
    private final EmployeeId companyId;
}