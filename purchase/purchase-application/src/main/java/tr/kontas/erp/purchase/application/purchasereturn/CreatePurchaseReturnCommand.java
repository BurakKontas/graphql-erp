package tr.kontas.erp.purchase.application.purchasereturn;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreatePurchaseReturnCommand(
        CompanyId companyId,
        String purchaseOrderId,
        String goodsReceiptId,
        String vendorId,
        String warehouseId,
        LocalDate returnDate,
        String reason,
        List<LineCommand> lines
) {
    public record LineCommand(
            String receiptLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal quantity,
            String lineReason
    ) {
    }
}

