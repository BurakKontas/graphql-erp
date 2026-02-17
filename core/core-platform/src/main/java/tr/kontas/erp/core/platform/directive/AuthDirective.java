package tr.kontas.erp.core.platform.directive;

import com.netflix.graphql.dgs.context.DgsContext;
import graphql.GraphqlErrorException;
import graphql.language.ArrayValue;
import graphql.language.StringValue;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLAppliedDirectiveArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.platform.context.Context;

import java.util.List;
import java.util.Set;

@Component
public class AuthDirective implements SchemaDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {

        List<String> requiredPermissions = extractPermissions(
                env.getAppliedDirective().getArgument("permissions")
        );

        String mode = extractMode(
                env.getAppliedDirective().getArgument("mode")
        );

        DataFetcher<?> originalFetcher = env.getFieldDataFetcher();

        DataFetcher<?> authFetcher = dataEnv -> {
            Context context = DgsContext.getCustomContext(dataEnv);

            if (context == null) {
                throw GraphqlErrorException.newErrorException()
                        .message("Unauthorized: No context")
                        .build();
            }

            if ("anonymous".equals(context.userId())) {
                throw GraphqlErrorException.newErrorException()
                        .message("Unauthorized: Authentication required")
                        .build();
            }

            if (requiredPermissions.isEmpty()) {
                return originalFetcher.get(dataEnv);
            }

            checkPermissions(context.permissions(), requiredPermissions, mode);

            return originalFetcher.get(dataEnv);
        };

        env.setFieldDataFetcher(authFetcher);
        return env.getElement();
    }

    private void checkPermissions(Set<String> userPermissions,
                                  List<String> requiredPermissions,
                                  String mode) {

        if (userPermissions == null || userPermissions.isEmpty()) {
            throw GraphqlErrorException.newErrorException()
                    .message("Access denied: No permissions")
                    .build();
        }

        boolean hasAccess = switch (mode.toUpperCase()) {
            case "ALL" -> userPermissions.containsAll(requiredPermissions);
            case "ANY" -> requiredPermissions.stream().anyMatch(userPermissions::contains);
            default -> throw new IllegalArgumentException("Invalid mode: " + mode);
        };

        if (!hasAccess) {
            throw GraphqlErrorException.newErrorException()
                    .message("Access denied: Insufficient permissions")
                    .build();
        }
    }

    private List<String> extractPermissions(GraphQLAppliedDirectiveArgument arg) {
        if (arg == null) {
            return List.of();
        }

        Object value = arg.getArgumentValue().getValue();

        if (value instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        }

        if (value instanceof ArrayValue arrayValue) {
            return arrayValue.getValues().stream()
                    .map(v -> ((StringValue) v).getValue())
                    .toList();
        }

        return List.of();
    }

    private String extractMode(GraphQLAppliedDirectiveArgument arg) {
        if (arg == null) {
            return "ANY";
        }

        Object value = arg.getArgumentValue().getValue();
        return value != null ? value.toString().toUpperCase() : "ANY";
    }
}