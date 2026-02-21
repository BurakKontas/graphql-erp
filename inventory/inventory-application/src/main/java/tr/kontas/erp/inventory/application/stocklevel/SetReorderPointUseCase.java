package tr.kontas.erp.inventory.application.stocklevel;

import java.math.BigDecimal;

public interface SetReorderPointUseCase {
    void execute(String stockLevelId, BigDecimal reorderPoint);
}
