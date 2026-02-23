package tr.kontas.erp.core.domain.idempotency;

import lombok.AccessLevel;
import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;

import java.time.LocalDateTime;

@Getter
public class Idempotency extends AggregateRoot<IdempotencyKey> {
    @Getter(AccessLevel.NONE)
    private final Object response;

    public Idempotency(IdempotencyKey idempotencyKey, Object response) {
        super(idempotencyKey);
        this.response = response;
    }

    @SuppressWarnings("unchecked")
    public <TResponse> TResponse getResponse() {
        TResponse result = (TResponse)response;

        registerEvent(new IdempotencyResponseRequestedEvent(id, result));

        return result;
    }
}
