package tr.kontas.erp.core.application.idempotency;

import tr.kontas.erp.core.domain.idempotency.Idempotency;

public interface InsertIdempotencyUseCase {
    Idempotency execute(InsertIdempotencyCommand command);
}
