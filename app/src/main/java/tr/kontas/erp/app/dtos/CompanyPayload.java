package tr.kontas.erp.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyPayload {
    private String tenantId;
    private String id;
    private String name;
}
