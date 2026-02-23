package tr.kontas.erp.core.application.idempotency;

import tr.kontas.erp.core.domain.idempotency.IdempotencyKey;

public record InsertIdempotencyCommand(IdempotencyKey idempotencyKey, Object response) {
}

