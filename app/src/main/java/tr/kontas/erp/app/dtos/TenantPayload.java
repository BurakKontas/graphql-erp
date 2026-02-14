package tr.kontas.erp.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TenantPayload {
    private String id;
    private String name;
    private String code;
}
