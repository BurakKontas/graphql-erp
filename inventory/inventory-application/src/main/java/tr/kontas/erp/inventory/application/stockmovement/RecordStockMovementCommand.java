package tr.kontas.erp.inventory.application.stockmovement;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecordStockMovementCommand(
        CompanyId companyId,
        String itemId,
        String warehouseId,
        String movementType,
        BigDecimal quantity,
        String referenceType,
        String referenceId,
        String note,
        LocalDate movementDate
) {
}
