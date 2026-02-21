package tr.kontas.erp.purchase.application.goodsreceipt;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateGoodsReceiptCommand(
        CompanyId companyId,
        String purchaseOrderId,
        String vendorId,
        String warehouseId,
        LocalDate receiptDate,
        String vendorDeliveryNote,
        List<LineCommand> lines
) {
    public record LineCommand(
            String poLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal quantity,
            String batchNote
    ) {
    }
}

