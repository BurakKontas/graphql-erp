package tr.kontas.erp.purchase.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptPostedEvent extends DomainEvent {
    private UUID goodsReceiptId;
    private UUID tenantId;
    private UUID companyId;
    private String receiptNumber;
    private String purchaseOrderId;
    private String warehouseId;
    private List<LineDetail> lines;

    @Override
    public UUID getAggregateId() {
        return goodsReceiptId;
    }


    @Override
    public String getAggregateType() {
        return "GoodsReceipt";
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineDetail {
        private String lineId;
        private String poLineId;
        private String itemId;
        private BigDecimal quantity;
    }
}