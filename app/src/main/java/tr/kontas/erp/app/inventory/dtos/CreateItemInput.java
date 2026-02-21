package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;

@Data
public class CreateItemInput {
    private String companyId;
    private String code;
    private String name;
    private String type;
    private String unitCode;
    private String categoryId;
    private Boolean allowNegativeStock;
}
