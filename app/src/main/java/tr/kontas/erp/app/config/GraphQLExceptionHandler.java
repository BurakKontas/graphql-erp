package tr.kontas.erp.app.config;

import com.netflix.graphql.types.errors.ErrorType;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.kernel.exception.DomainException;
import tr.kontas.erp.inventory.domain.exception.InventoryDomainException;
import tr.kontas.erp.sales.domain.exception.SalesDomainException;

import java.util.concurrent.CompletionException;

@Slf4j
@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
        Throwable exception = ex;

        if (exception instanceof CompletionException && exception.getCause() != null) {
            exception = exception.getCause();
        }

        ErrorType errorType = mapErrorType(exception);
        String message = exception.getMessage() != null
                ? exception.getMessage()
                : exception.getClass().getSimpleName();

        log.warn("GraphQL error [{}] at {}: {}", errorType, env.getExecutionStepInfo().getPath(), message);

        return TypedGraphQLError.newBuilder()
                .message(message)
                .errorType(errorType)
                .path(env.getExecutionStepInfo().getPath())
                .build();
    }

    private static ErrorType mapErrorType(Throwable exception) {
        return switch (exception) {
            case IllegalArgumentException _ -> ErrorType.BAD_REQUEST;
            case IllegalStateException _ -> ErrorType.FAILED_PRECONDITION;
            case DomainException _ -> ErrorType.BAD_REQUEST;
            case SalesDomainException _ -> ErrorType.BAD_REQUEST;
            case InventoryDomainException _ -> ErrorType.BAD_REQUEST;
            case SecurityException _ -> ErrorType.PERMISSION_DENIED;
            case jakarta.persistence.EntityNotFoundException _ -> ErrorType.NOT_FOUND;
            default -> ErrorType.INTERNAL;
        };
    }
}
