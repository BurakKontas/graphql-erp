package tr.kontas.erp.app.shipment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DeliveryOrderLinePayload {
    private String id;
    private String salesOrderLineId;
    private String itemId;
    private String itemDescription;
    private String unitCode;
    private BigDecimal orderedQty;
    private BigDecimal shippedQty;
    private BigDecimal remainingQty;
}

