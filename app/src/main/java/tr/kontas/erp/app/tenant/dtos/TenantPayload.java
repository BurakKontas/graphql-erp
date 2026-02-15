package tr.kontas.erp.app.tenant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TenantPayload {
    private String id;
    private String name;
    private String code;
}
