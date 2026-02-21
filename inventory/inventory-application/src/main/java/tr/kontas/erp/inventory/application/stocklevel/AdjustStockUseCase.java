package tr.kontas.erp.inventory.application.stocklevel;

public interface AdjustStockUseCase {
    void execute(AdjustStockCommand command);
}
