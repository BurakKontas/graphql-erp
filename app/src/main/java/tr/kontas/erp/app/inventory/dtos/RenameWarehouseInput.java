package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;

@Data
public class RenameWarehouseInput {
    private String warehouseId;
    private String name;
}
