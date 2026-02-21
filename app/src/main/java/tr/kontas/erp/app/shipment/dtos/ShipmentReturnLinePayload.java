package tr.kontas.erp.app.shipment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ShipmentReturnLinePayload {
    private String id;
    private String shipmentLineId;
    private String itemId;
    private String itemDescription;
    private String unitCode;
    private BigDecimal quantity;
    private String lineReason;
}

