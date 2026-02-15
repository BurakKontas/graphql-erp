package tr.kontas.erp.core.domain.department;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

@Getter
@RequiredArgsConstructor
public class DepartmentCreatedEvent extends DomainEvent {
    private final DepartmentId companyId;
}