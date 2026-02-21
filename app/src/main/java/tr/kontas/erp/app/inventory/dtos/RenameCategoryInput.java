package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;

@Data
public class RenameCategoryInput {
    private String categoryId;
    private String name;
}
