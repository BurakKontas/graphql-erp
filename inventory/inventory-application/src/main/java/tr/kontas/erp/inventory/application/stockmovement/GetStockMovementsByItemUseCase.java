package tr.kontas.erp.inventory.application.stockmovement;

import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.stockmovement.StockMovement;

import java.util.List;

public interface GetStockMovementsByItemUseCase {
    List<StockMovement> execute(ItemId itemId);
}
