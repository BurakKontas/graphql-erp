package tr.kontas.erp.purchase.application.purchaserequest;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreatePurchaseRequestCommand(
        CompanyId companyId,
        String requestedBy,
        LocalDate requestDate,
        LocalDate neededBy,
        String notes,
        List<LineCommand> lines
) {
    public record LineCommand(
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal quantity,
            String preferredVendorId,
            String notes
    ) {
    }
}

