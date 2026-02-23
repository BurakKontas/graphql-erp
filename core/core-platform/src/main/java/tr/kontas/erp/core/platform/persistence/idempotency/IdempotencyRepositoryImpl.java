package tr.kontas.erp.core.platform.persistence.idempotency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.department.DepartmentId;
import tr.kontas.erp.core.domain.idempotency.Idempotency;
import tr.kontas.erp.core.domain.idempotency.IdempotencyKey;
import tr.kontas.erp.core.domain.idempotency.IdempotencyRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class IdempotencyRepositoryImpl implements IdempotencyRepository {
    private final JpaIdempotencyRepository jpaRepository;

    @Override
    public void save(Idempotency entity) {
        jpaRepository.save(IdempotencyMapper.toEntity(entity));
    }

    @Override
    public Optional<Idempotency> get(IdempotencyKey idempotencyKey) {
        return jpaRepository.findById(idempotencyKey.asUUID())
                .map(IdempotencyMapper::toDomain);
    }

    @Override
    public void remove(IdempotencyKey idempotencyKey) {
        jpaRepository.removeByIdempotencyKey(idempotencyKey.asUUID());
    }

    @Override
    public List<Idempotency> getByIds(List<IdempotencyKey> idempotencyKeys) {
        if (idempotencyKeys == null || idempotencyKeys.isEmpty()) return List.of();

        List<UUID> uuidList = idempotencyKeys.stream()
                .map(IdempotencyKey::asUUID)
                .toList();

        return jpaRepository.findByIdempotencyKeyIn(uuidList)
                .stream()
                .map(IdempotencyMapper::toDomain)
                .toList();
    }
}
