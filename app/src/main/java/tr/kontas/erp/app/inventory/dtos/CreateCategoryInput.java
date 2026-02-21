package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;

@Data
public class CreateCategoryInput {
    private String companyId;
    private String name;
    private String parentCategoryId;
}
