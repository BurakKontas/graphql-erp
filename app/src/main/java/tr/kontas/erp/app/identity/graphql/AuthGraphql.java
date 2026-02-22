package tr.kontas.erp.app.identity.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.context.DgsContext;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import tr.kontas.erp.app.identity.dtos.AuthPayload;
import tr.kontas.erp.app.identity.dtos.LoginInput;
import tr.kontas.erp.app.identity.dtos.RegisterInput;
import tr.kontas.erp.app.identity.dtos.RegisterPayload;
import tr.kontas.erp.core.application.identity.*;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.context.Context;
import tr.kontas.erp.core.platform.service.identity.AuthenticationApplicationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DgsComponent
@RequiredArgsConstructor
public class AuthGraphql {

    private final AuthenticationApplicationService authenticationService;
    private final TenantRepository tenantRepository;
    private final RegisterUserUseCase registerUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;

    @DgsMutation
    public AuthPayload login(@InputArgument("input") LoginInput input,
                             DataFetchingEnvironment env) {

        Context context = DgsContext.getCustomContext(env);

        if (context == null || context.tenantId() == null) {
            throw new IllegalArgumentException("Tenant context required. Provide X-TENANT header.");
        }

        String tenantId = context.tenantId().toString();
        AuthProviderType providerType = resolveAuthMode(tenantId);

        Map<String, Object> attributes = buildAttributes(input, providerType);

        AuthenticationCommand command = new AuthenticationCommand(
                context.tenantId().toString(),
                providerType,
                null,
                null,
                attributes
        );

        List<AuthToken> tokens = authenticationService.authenticate(command);

        if (tokens == null || tokens.size() < 2) {
            throw new IllegalStateException("Authentication failed: invalid token response");
        }

        AuthToken accessToken = tokens.get(0);
        AuthToken refreshToken = tokens.get(1);

        return new AuthPayload(
                accessToken.accessToken(),
                refreshToken.accessToken(),
                accessToken.expiresIn()
        );
    }

    @DgsMutation
    public AuthPayload refreshToken(@InputArgument("refreshToken") String refreshToken,
                                    DataFetchingEnvironment env) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token is required");
        }

        List<AuthToken> tokens = refreshTokenUseCase.execute(refreshToken);

        if (tokens == null || tokens.size() < 2) {
            throw new IllegalStateException("Token refresh failed: invalid token response");
        }

        AuthToken accessToken = tokens.get(0);
        AuthToken newRefreshToken = tokens.get(1);

        return new AuthPayload(
                accessToken.accessToken(),
                newRefreshToken.accessToken(),
                accessToken.expiresIn()
        );
    }

    @DgsMutation
    public Boolean logout(DataFetchingEnvironment env) {
        Context context = DgsContext.getCustomContext(env);

        if (context == null || context.userId() == null) {
            throw new IllegalArgumentException("Authentication required");
        }

        logoutUseCase.execute(UserId.of(context.userId()));

        return true;
    }

    @DgsMutation
    public RegisterPayload register(@InputArgument("input") RegisterInput input,
                                    DataFetchingEnvironment env) {
        Context context = DgsContext.getCustomContext(env);

        if (context == null || context.tenantId() == null) {
            throw new IllegalArgumentException("Tenant context required. Provide X-TENANT header.");
        }

        if (input.getUsername() == null || input.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (input.getPassword() == null || input.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        RegisterCommand command = new RegisterCommand(
                context.tenantId().toString(),
                input.getUsername(),
                input.getPassword()
        );

        UserId userId = registerUserUseCase.execute(command);

        return new RegisterPayload(userId.getValue().toString(), input.getUsername());
    }

    private AuthProviderType resolveAuthMode(String tenantId) {
        return tenantRepository.findAuthModeById(TenantId.of(tenantId))
                .orElse(AuthProviderType.LOCAL);
    }

    private Map<String, Object> buildAttributes(LoginInput input, AuthProviderType providerType) {
        Map<String, Object> attributes = new HashMap<>();

        switch (providerType) {
            case LOCAL, LDAP -> {
                if (input.getUsername() == null || input.getPassword() == null) {
                    throw new IllegalArgumentException("Username and password required for " + providerType);
                }
                attributes.put("username", input.getUsername());
                attributes.put("password", input.getPassword());
            }
            case OIDC -> {
                if (input.getIdToken() == null) {
                    throw new IllegalArgumentException("idToken required for OIDC");
                }
                attributes.put("id_token", input.getIdToken());
            }
        }

        return attributes;
    }
}
