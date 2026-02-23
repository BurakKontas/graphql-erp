package tr.kontas.erp.core.platform.directive;

import com.netflix.graphql.dgs.context.DgsContext;
import graphql.GraphqlErrorException;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.platform.context.Context;
import tr.kontas.erp.core.application.idempotency.CheckIdempotencyUseCase;
import tr.kontas.erp.core.application.idempotency.InsertIdempotencyCommand;
import tr.kontas.erp.core.application.idempotency.InsertIdempotencyUseCase;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyDirective implements SchemaDirectiveWiring {

    private final CheckIdempotencyUseCase checkIdempotencyUseCase;
    private final InsertIdempotencyUseCase insertIdempotencyUseCase;

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
        DataFetcher<?> originalFetcher = env.getFieldDataFetcher();

        DataFetcher<?> df = dataEnv -> {
            Context context = DgsContext.getCustomContext(dataEnv);

            if (context == null) {
                return originalFetcher.get(dataEnv);
            }

            String key = context.idempotencyKey();
            if (key == null || key.isBlank()) {
                return originalFetcher.get(dataEnv);
            }

            try {
                var existing = checkIdempotencyUseCase.execute(tr.kontas.erp.core.domain.idempotency.IdempotencyKey.of(key));
                if (existing != null) {
                    return existing.getResponse();
                }

                Object result = originalFetcher.get(dataEnv);

                CompletableFuture.runAsync(() -> {
                    try {
                        insertIdempotencyUseCase.execute(new InsertIdempotencyCommand(tr.kontas.erp.core.domain.idempotency.IdempotencyKey.of(key), result));
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });

                return result;
            } catch (Exception ex) {
                throw GraphqlErrorException.newErrorException()
                        .message("Idempotency check failed: " + ex.getMessage())
                        .build();
            }
        };

        env.setFieldDataFetcher(df);
        return env.getElement();
    }
}
