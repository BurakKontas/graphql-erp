package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReserveStockInput {
    private String itemId;
    private String warehouseId;
    private BigDecimal quantity;
    private String referenceId;
}
