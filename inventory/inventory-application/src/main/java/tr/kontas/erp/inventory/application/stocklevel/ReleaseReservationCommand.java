package tr.kontas.erp.inventory.application.stocklevel;

import java.math.BigDecimal;

public record ReleaseReservationCommand(
        String itemId,
        String warehouseId,
        BigDecimal quantity,
        String referenceId
) {
}
