package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;

@Data
public class CreateWarehouseInput {
    private String companyId;
    private String code;
    private String name;
}
