package tr.kontas.erp.core.platform.service.idempotency;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.idempotency.CheckIdempotencyUseCase;
import tr.kontas.erp.core.application.idempotency.GetIdempotenciesByIdsUseCase;
import tr.kontas.erp.core.application.idempotency.InsertIdempotencyCommand;
import tr.kontas.erp.core.application.idempotency.InsertIdempotencyUseCase;
import tr.kontas.erp.core.domain.idempotency.Idempotency;
import tr.kontas.erp.core.domain.idempotency.IdempotencyKey;
import tr.kontas.erp.core.domain.idempotency.IdempotencyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdempotencyService implements
    CheckIdempotencyUseCase,
    InsertIdempotencyUseCase,
    GetIdempotenciesByIdsUseCase {

    private final IdempotencyRepository repository;

    @Override
    public Idempotency execute(IdempotencyKey idempotencyKey) {
        return repository.get(idempotencyKey)
                .orElse(null);
    }

    @Override
    @Transactional
    public Idempotency execute(InsertIdempotencyCommand command) {
        Idempotency idempotency = new Idempotency(
            command.idempotencyKey(),
            command.response()
        );

        repository.save(idempotency);

        return idempotency;
    }

    @Override
    public List<Idempotency> execute(List<IdempotencyKey> keys) {
        return repository.getByIds(keys);
    }
}
