package tr.kontas.erp.core.platform.directive;

import com.netflix.graphql.dgs.context.DgsContext;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.application.audit.AuditWriter;
import tr.kontas.erp.core.domain.audit.AuditEntry;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.context.Context;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditDirective implements SchemaDirectiveWiring {

    private final AuditWriter auditWriter;
    private final TenantRepository tenantRepository;

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
        String fieldName = env.getFieldDefinition().getName();
        DataFetcher<?> originalFetcher = env.getFieldDataFetcher();

        DataFetcher<?> auditFetcher = dataEnv -> {
            Context context = DgsContext.getCustomContext(dataEnv);
            boolean authenticated = context != null && !"anonymous".equals(context.userId());

            if (!authenticated) {
                if (context != null && context.tenantId() != null) {
                    try {
                        var tenant = tenantRepository.findById(TenantId.of(context.tenantId()));
                        if (tenant.isEmpty() || !tenant.get().isAuditUnauthenticated()) {
                            return originalFetcher.get(dataEnv);
                        }
                    } catch (Exception e) {
                        return originalFetcher.get(dataEnv);
                    }
                } else {
                    return originalFetcher.get(dataEnv);
                }
            }

            Object result;
            Exception error = null;
            Instant start = Instant.now();

            try {
                result = originalFetcher.get(dataEnv);
            } catch (Exception e) {
                error = e;
                throw e;
            } finally {
                try {
                    writeAuditEntry(context, fieldName, error);
                } catch (Exception e) {
                    log.warn("Failed to write audit entry for {}: {}", fieldName, e.getMessage());
                }
            }

            return result;
        };

        env.setFieldDataFetcher(auditFetcher);
        return env.getElement();
    }

    private void writeAuditEntry(Context context, String fieldName, Exception error) {
        String userId = context != null ? context.userId() : "anonymous";
        UUID tenantId = context != null ? context.tenantId() : null;

        String status = error != null ? "ERROR: " + error.getMessage() : "SUCCESS";

        AuditEntry entry = new AuditEntry(
                UUID.randomUUID(),
                "GRAPHQL_REQUEST",
                tenantId,
                null,
                "GRAPHQL",
                "GraphQL",
                fieldName,
                "EXECUTE",
                null,
                userId,
                null,
                null,
                null,
                Instant.now(),
                null,
                status,
                List.of(),
                null,
                null
        );

        auditWriter.write(entry);
    }
}

