package tr.kontas.erp.core.application.identity;

import java.time.Instant;

public record AuthToken(
        String accessToken,
        String tokenType,
        long expiresIn,
        Instant issuedAt
) {

    public static AuthToken bearer(
            String token,
            long expiresInSeconds
    ) {
        return new AuthToken(
                token,
                "Bearer",
                expiresInSeconds,
                Instant.now()
        );
    }
}