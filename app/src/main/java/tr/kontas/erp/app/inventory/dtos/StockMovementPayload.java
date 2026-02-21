package tr.kontas.erp.app.inventory.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StockMovementPayload {
    private String id;
    private String companyId;
    private String itemId;
    private String warehouseId;
    private String movementType;
    private BigDecimal quantity;
    private String referenceType;
    private String referenceId;
    private String note;
    private String movementDate;
    private String createdAt;
}
