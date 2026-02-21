package tr.kontas.erp.inventory.application.stocklevel;

import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.stocklevel.StockLevel;

import java.util.List;

public interface GetStockLevelsByItemUseCase {
    List<StockLevel> execute(ItemId itemId);
}
