package tr.kontas.erp.inventory.application.stocklevel;

import java.math.BigDecimal;

public record AdjustStockCommand(
        String itemId,
        String warehouseId,
        BigDecimal adjustment
) {
}
