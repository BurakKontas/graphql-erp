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
public class PurchaseReturnPostedEvent extends DomainEvent {
    private UUID purchaseReturnId;
    private UUID tenantId;
    private UUID companyId;
    private String returnNumber;
    private String purchaseOrderId;
    private String warehouseId;
    private List<LineDetail> lines;

    @Override
    public UUID getAggregateId() {
        return purchaseReturnId;
    }


    @Override
    public String getAggregateType() {
        return "PurchaseReturn";
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineDetail {
        private String lineId;
        private String itemId;
        private BigDecimal quantity;
    }
}