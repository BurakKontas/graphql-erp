package tr.kontas.erp.app.identity.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthPayload {
    private String accessToken;
    private String refreshToken;
    private Long expiresAt;
}
