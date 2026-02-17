package tr.kontas.erp.core.platform.context;

import com.netflix.graphql.dgs.reactive.DgsReactiveCustomContextBuilderWithRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import tr.kontas.erp.core.application.identity.JwtPrincipal;
import tr.kontas.erp.core.application.identity.JwtService;
import tr.kontas.erp.core.domain.identity.repositories.UserRepository;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.domain.tenant.TenantCode;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ReactiveContextBuilder implements DgsReactiveCustomContextBuilderWithRequest<Context> {

    private final TenantRepository tenantRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public @NonNull Mono<Context> build(@Nullable Map<String, ?> extensions,
                                        @Nullable HttpHeaders headers,
                                        @Nullable ServerRequest serverRequest) {

        String tenantCode = extractHeader(headers, "X-TENANT");

        if (tenantCode == null) {
            return Mono.just(new Context("anonymous", null, Set.of()));
        }

        return Mono.fromCallable(() ->
                tenantRepository.findIdByCode(new TenantCode(tenantCode))
                        .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantCode))
        ).flatMap(tenantId -> {
            TenantContext.setTenantIdentifier(tenantId.asUUID().toString());

            String bearer = extractHeader(headers, "Authorization");

            if (bearer != null && bearer.startsWith("Bearer ")) {
                return handleJwtAuth(bearer.substring(7), tenantId.asUUID());
            }

            return Mono.just(new Context("anonymous", tenantId.asUUID(), Set.of()));
        });
    }

    private Mono<Context> handleJwtAuth(String token, java.util.UUID tenantId) {
        return Mono.fromCallable(() -> {
            JwtPrincipal principal = jwtService.parse(token);

            // Auth version check
            long currentVersion = userRepository
                    .findAuthVersionById(UserId.of(principal.userId()))
                    .orElseThrow(() -> new SecurityException("User not found"));

            if (currentVersion != principal.authVersion()) {
                throw new SecurityException("Token has been invalidated");
            }

            return new Context(
                    principal.userId().toString(),
                    tenantId,
                    principal.permissions()
            );
        });
    }

    private String extractHeader(HttpHeaders headers, String name) {
        return headers != null ? headers.getFirst(name) : null;
    }
}