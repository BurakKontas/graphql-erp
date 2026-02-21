package tr.kontas.erp.app.inventory.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WarehousePayload {
    private String id;
    private String companyId;
    private String code;
    private String name;
    private boolean active;
}
