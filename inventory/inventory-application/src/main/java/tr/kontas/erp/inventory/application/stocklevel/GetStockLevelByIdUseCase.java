package tr.kontas.erp.inventory.application.stocklevel;

import tr.kontas.erp.inventory.domain.stocklevel.StockLevel;
import tr.kontas.erp.inventory.domain.stocklevel.StockLevelId;

public interface GetStockLevelByIdUseCase {
    StockLevel execute(StockLevelId id);
}
