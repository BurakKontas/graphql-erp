package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdjustStockInput {
    private String itemId;
    private String warehouseId;
    private BigDecimal adjustment;
}
