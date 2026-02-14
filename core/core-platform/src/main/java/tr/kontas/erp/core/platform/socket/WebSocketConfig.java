package tr.kontas.erp.core.platform.socket;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebSocketGraphQlInterceptor;
import org.springframework.graphql.server.WebSocketSessionInfo;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Configuration
public class WebSocketConfig {

    @Bean
    public WebSocketGraphQlInterceptor webSocketGraphQlInterceptor() {
        return new WebSocketGraphQlInterceptor() {

            @NonNull
            @Override
            public Mono<Object> handleConnectionInitialization(
                    @NonNull WebSocketSessionInfo sessionInfo,
                    @NonNull Map<String, Object> payload) {

                log.atInfo().log("Decoding jwt");
                return Mono.empty();
            }
        };
    }
}
