package tr.kontas.erp.core.platform.service.identity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.application.identity.AuthToken;
import tr.kontas.erp.core.application.identity.JwtService;
import tr.kontas.erp.core.application.identity.RefreshTokenUseCase;
import tr.kontas.erp.core.application.identity.TokenResult;
import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AuthToken> execute(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token is required");
        }

        // Parse refresh token to get user id and authVersion
        JwtService.RefreshTokenInfo tokenInfo = jwtService.parseRefreshTokenWithAuthVersion(refreshToken);

        // Load user from database
        UserAccount user = userRepository.findById(tokenInfo.userId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Check if user is still active
        if (!user.isActive()) {
            throw new SecurityException("User is inactive");
        }

        if (user.isLocked()) {
            throw new SecurityException("User is locked");
        }

        // Validate authVersion - if user logged out, authVersion will be incremented
        // and old refresh tokens will be invalid
        if (tokenInfo.authVersion() < user.getAuthVersion()) {
            throw new SecurityException("Token has been invalidated. Please login again.");
        }

        // Generate new tokens
        TokenResult tokenResult = jwtService.generate(user);

        AuthToken accessToken = AuthToken.bearer(tokenResult.accessToken(), tokenResult.accessTokenExpiresAt());
        AuthToken newRefreshToken = AuthToken.bearer(tokenResult.refreshToken(), tokenResult.refreshTokenExpiresAt());

        return List.of(accessToken, newRefreshToken);
    }
}
