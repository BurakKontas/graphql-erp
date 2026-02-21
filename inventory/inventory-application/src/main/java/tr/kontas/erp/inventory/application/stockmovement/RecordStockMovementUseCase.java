package tr.kontas.erp.inventory.application.stockmovement;

import tr.kontas.erp.inventory.domain.stockmovement.StockMovementId;

public interface RecordStockMovementUseCase {
    StockMovementId execute(RecordStockMovementCommand command);
}
