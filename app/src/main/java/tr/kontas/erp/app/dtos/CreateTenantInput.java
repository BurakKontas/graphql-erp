package tr.kontas.erp.app.dtos;

import lombok.Data;

@Data
public class CreateTenantInput {
    private String name;
    private String code;
}
