package tr.kontas.erp.core.platform.service.identity;

import org.springframework.stereotype.Component;
import tr.kontas.erp.core.application.identity.AuthenticationProvider;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AuthenticationProviderRegistry {

    private final Map<AuthProviderType, AuthenticationProvider> providers;

    public AuthenticationProviderRegistry(List<AuthenticationProvider> providers) {
        this.providers = providers.stream()
                .collect(Collectors.toMap(
                        this::extractProviderType,
                        Function.identity(),
                        (existing, replacement) -> {
                            throw new IllegalStateException(
                                    "Duplicate provider for type: " + extractProviderType(existing));
                        }
                ));
    }

    public AuthenticationProvider getProvider(AuthProviderType type) {
        AuthenticationProvider provider = providers.get(type);
        if (provider == null) {
            throw new IllegalArgumentException("Provider not supported: " + type);
        }
        return provider;
    }

    public boolean supports(AuthProviderType type) {
        return providers.containsKey(type);
    }

    private AuthProviderType extractProviderType(AuthenticationProvider provider) {
        for (AuthProviderType type : AuthProviderType.values()) {
            if (provider.supports(type)) {
                return type;
            }
        }
        throw new IllegalStateException("Provider does not support any known type: " + provider.getClass().getName());
    }
}
