package tr.kontas.erp.app.inventory.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryPayload {
    private String id;
    private String companyId;
    private String name;
    private String parentCategoryId;
    private boolean active;
}
