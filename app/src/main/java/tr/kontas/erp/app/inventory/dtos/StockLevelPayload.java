package tr.kontas.erp.app.inventory.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StockLevelPayload {
    private String id;
    private String companyId;
    private String itemId;
    private String warehouseId;
    private BigDecimal quantityOnHand;
    private BigDecimal quantityReserved;
    private BigDecimal quantityAvailable;
    private BigDecimal reorderPoint;
    private boolean allowNegativeStock;
}
