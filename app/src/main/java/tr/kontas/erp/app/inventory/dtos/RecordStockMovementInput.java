package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecordStockMovementInput {
    private String companyId;
    private String itemId;
    private String warehouseId;
    private String movementType;
    private BigDecimal quantity;
    private String referenceType;
    private String referenceId;
    private String note;
    private String movementDate;
}
