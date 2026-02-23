package tr.kontas.erp.core.application.idempotency;

import tr.kontas.erp.core.domain.idempotency.Idempotency;
import tr.kontas.erp.core.domain.idempotency.IdempotencyKey;

public interface CheckIdempotencyUseCase {
    Idempotency execute(IdempotencyKey idempotencyKey);
}
