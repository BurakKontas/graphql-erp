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

@Component
public class AuthDirective implements SchemaDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {

        List<String> requiredRoles = extractRoles(env);

        DataFetcher<?> originalFetcher = env.getFieldDataFetcher();

        DataFetcher<?> authFetcher = dataEnv -> {
            Context context = DgsContext.getCustomContext(dataEnv);

            if (context == null) {
                throw GraphqlErrorException.newErrorException()
                        .message("Unauthorized")
                        .build();
            }

            String userRole = context.getRole();
            if (!requiredRoles.contains(userRole)) {
                throw GraphqlErrorException.newErrorException()
                        .message("Forbidden - Required roles: " + requiredRoles)
                        .build();
            }

            return originalFetcher.get(dataEnv);
        };

        env.setFieldDataFetcher(authFetcher);
        return env.getElement();
    }

    private List<String> extractRoles(SchemaDirectiveWiringEnvironment<?> env) {

        GraphQLAppliedDirectiveArgument arg =
                env.getAppliedDirective().getArgument("roles");

        if (arg == null) return List.of();

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
}
