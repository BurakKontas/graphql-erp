package tr.kontas.erp.inventory.domain.exception;


import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.math.BigDecimal;

public class StockInsufficientException extends InventoryDomainException {

    public StockInsufficientException(ItemId itemId, WarehouseId warehouseId,
                                      BigDecimal requested, BigDecimal available) {
        super(("Insufficient stock for item '%s' in warehouse '%s'. " +
                "Requested: %s, Available: %s")
                .formatted(itemId, warehouseId, requested, available));
    }
}
