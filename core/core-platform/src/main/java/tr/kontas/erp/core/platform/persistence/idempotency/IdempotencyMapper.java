package tr.kontas.erp.core.platform.persistence.idempotency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tr.kontas.erp.core.domain.idempotency.Idempotency;
import tr.kontas.erp.core.domain.idempotency.IdempotencyKey;
import tr.kontas.erp.core.platform.configuration.JacksonProvider;

public class IdempotencyMapper {

    private static final ObjectMapper mapper = JacksonProvider.get();

    public static Idempotency toDomain(IdempotencyJpaEntity entity) {
        try {
            var response = mapper.readValue(entity.getResponse(), Object.class);

            return new Idempotency(
                    IdempotencyKey.of(entity.getIdempotencyKey()),
                    response
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize idempotency response", e);
        }
    }

    public static IdempotencyJpaEntity toEntity(Idempotency domain) {
        try {
            var response = mapper.writeValueAsString(domain.getResponse());

            var entity  = new IdempotencyJpaEntity();
            entity.setIdempotencyKey(domain.getId().asUUID());
            entity.setResponse(response);

            return entity;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize idempotency response", e);
        }
    }
}
