package tr.kontas.erp.core.application.identity;

import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;

public interface JwtService {
    TokenResult generate(UserAccount user);

    boolean validate(String token);

    JwtPrincipal parse(String token);

    UserId parseRefreshToken(String refreshToken);

    RefreshTokenInfo parseRefreshTokenWithAuthVersion(String refreshToken);

    record RefreshTokenInfo(UserId userId, long authVersion) {
    }
}