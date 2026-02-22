package tr.kontas.erp.core.platform.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebSocketGraphQlInterceptor;
import org.springframework.graphql.server.WebSocketSessionInfo;
import reactor.core.publisher.Mono;
import tr.kontas.erp.core.application.identity.JwtService;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig {

    private final JwtService jwtService;

    @Bean
    public WebSocketGraphQlInterceptor webSocketGraphQlInterceptor() {
        return new WebSocketGraphQlInterceptor() {

            @NonNull
            @Override
            public Mono<Object> handleConnectionInitialization(
                    @NonNull WebSocketSessionInfo sessionInfo,
                    @NonNull Map<String, Object> payload) {

                Object token = payload.get("Authorization");
                if (token == null) {
                    token = payload.get("authorization");
                }
                if (token == null) {
                    token = payload.get("token");
                }

                if (token == null) {
                    return Mono.error(new SecurityException("WebSocket: Authorization token required"));
                }

                String bearer = token.toString();
                if (bearer.startsWith("Bearer ")) {
                    bearer = bearer.substring(7);
                }

                try {
                    jwtService.parse(bearer);
                    log.debug("WebSocket connection authenticated");
                    return Mono.just(payload);
                } catch (Exception e) {
                    log.warn("WebSocket authentication failed: {}", e.getMessage());
                    return Mono.error(new SecurityException("WebSocket: Invalid or expired token"));
                }
            }
        };
    }
}
