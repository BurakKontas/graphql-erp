package tr.kontas.erp.app.identity.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProvisionExternalIdentityPayload {
    private String userId;
    private String username;
    private String provider;
    private String externalId;
}
