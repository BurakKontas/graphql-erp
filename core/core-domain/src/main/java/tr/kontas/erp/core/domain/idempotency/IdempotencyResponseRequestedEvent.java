package tr.kontas.erp.core.domain.idempotency;

import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.domain.employee.EmployeeId;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class IdempotencyResponseRequestedEvent extends DomainEvent {
    private UUID idempotencyKey;
    private Object response;

    public IdempotencyResponseRequestedEvent(IdempotencyKey idempotencyKey, Object response) {
        this.idempotencyKey = idempotencyKey.asUUID();
        this.response = response;
    }

    @Override
    public UUID getAggregateId() {
        return idempotencyKey;
    }

    @Override
    public String getAggregateType() {
        return "Idempotency";
    }
}