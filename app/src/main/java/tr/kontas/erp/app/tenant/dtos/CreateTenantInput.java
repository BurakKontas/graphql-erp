package tr.kontas.erp.app.tenant.dtos;

import lombok.Data;

@Data
public class CreateTenantInput {
    private String name;
    private String code;
}
