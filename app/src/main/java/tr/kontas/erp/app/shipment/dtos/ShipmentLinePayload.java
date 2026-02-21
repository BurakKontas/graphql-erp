package tr.kontas.erp.app.shipment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ShipmentLinePayload {
    private String id;
    private String deliveryOrderLineId;
    private String itemId;
    private String itemDescription;
    private String unitCode;
    private BigDecimal quantity;
}

