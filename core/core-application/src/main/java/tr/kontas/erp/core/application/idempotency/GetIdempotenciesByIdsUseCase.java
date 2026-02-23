package tr.kontas.erp.core.application.idempotency;

import tr.kontas.erp.core.domain.idempotency.Idempotency;
import tr.kontas.erp.core.domain.idempotency.IdempotencyKey;

import java.util.List;

public interface GetIdempotenciesByIdsUseCase {
    List<Idempotency> execute(List<IdempotencyKey> keys);
}
