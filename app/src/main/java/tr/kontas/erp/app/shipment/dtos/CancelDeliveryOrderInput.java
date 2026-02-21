package tr.kontas.erp.app.shipment.dtos;

import lombok.Data;

@Data
public class CancelDeliveryOrderInput {
    private String deliveryOrderId;
    private String reason;
}

