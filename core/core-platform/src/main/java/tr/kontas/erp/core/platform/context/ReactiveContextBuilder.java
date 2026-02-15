package tr.kontas.erp.core.platform.context;

import com.netflix.graphql.dgs.reactive.DgsReactiveCustomContextBuilderWithRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import tr.kontas.erp.core.domain.tenant.TenantCode;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReactiveContextBuilder implements DgsReactiveCustomContextBuilderWithRequest<Context> {

    private final TenantRepository tenantRepository;

    @Override
    public @NonNull Mono<Context> build(@Nullable Map<String, ?> extensions,
                                        @Nullable HttpHeaders headers,
                                        @Nullable ServerRequest serverRequest) {
        String userId = headers != null ? headers.getFirst("x-user-id") : "anonymous";
        String role = headers != null ? headers.getFirst("x-user-role") : "peasant";
        String tenantCode = headers != null ? headers.getFirst("X-TENANT") : null;

        if (tenantCode == null || tenantCode.isBlank()) {
            return Mono.just(new Context(userId, role, null));
        }

        return Mono.fromCallable(() ->
                tenantRepository.findIdByCode(new TenantCode(tenantCode))
                        .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantCode))
        ).map(tenantId -> {
            TenantContext.setTenantIdentifier(tenantId.asUUID().toString());

            return new Context(userId, role, tenantId.asUUID());
        });
    }
}