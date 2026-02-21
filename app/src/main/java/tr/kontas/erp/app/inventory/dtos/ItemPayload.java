package tr.kontas.erp.app.inventory.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemPayload {
    private String id;
    private String companyId;
    private String code;
    private String name;
    private String type;
    private String unitCode;
    private String categoryId;
    private boolean allowNegativeStock;
    private boolean active;
}
