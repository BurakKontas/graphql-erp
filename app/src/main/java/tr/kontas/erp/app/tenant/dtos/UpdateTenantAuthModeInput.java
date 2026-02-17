package tr.kontas.erp.app.tenant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTenantAuthModeInput {
    private String tenantId;
    private String authMode;
}
