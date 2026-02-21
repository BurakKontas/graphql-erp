package tr.kontas.erp.app.shipment.dtos;

import lombok.Data;

@Data
public class SetTrackingInfoInput {
    private String shipmentId;
    private String carrierName;
    private String trackingNumber;
}

