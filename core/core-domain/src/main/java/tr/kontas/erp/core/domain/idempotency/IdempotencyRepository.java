package tr.kontas.erp.core.domain.idempotency;

import tr.kontas.erp.core.kernel.domain.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface IdempotencyRepository extends Repository<Idempotency, IdempotencyKey> {
    void save(Idempotency entity);
    Optional<Idempotency> get(IdempotencyKey idempotencyKey);
    void remove(IdempotencyKey idempotencyKey);
    List<Idempotency> getByIds(List<IdempotencyKey> idempotencyKeys);
}
