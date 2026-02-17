package tr.kontas.erp.core.application.identity;

import java.util.List;

public interface RefreshTokenUseCase {
    List<AuthToken> execute(String refreshToken);
}
