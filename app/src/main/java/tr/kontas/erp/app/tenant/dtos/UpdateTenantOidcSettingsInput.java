package tr.kontas.erp.app.tenant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTenantOidcSettingsInput {
    private String tenantId;
    private String issuer;
    private String audience;
    private String jwkSetUri;
    private Long clockSkewSeconds;
}
