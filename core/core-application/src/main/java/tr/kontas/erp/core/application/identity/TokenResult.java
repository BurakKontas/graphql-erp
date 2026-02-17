package tr.kontas.erp.core.application.identity;


public record TokenResult(
        String accessToken,
        long accessTokenExpiresAt,
        String refreshToken,
        long refreshTokenExpiresAt
) {
}