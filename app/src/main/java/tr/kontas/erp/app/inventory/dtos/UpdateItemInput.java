package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;

@Data
public class UpdateItemInput {
    private String itemId;
    private String name;
    private String unitCode;
    private String categoryId;
}
