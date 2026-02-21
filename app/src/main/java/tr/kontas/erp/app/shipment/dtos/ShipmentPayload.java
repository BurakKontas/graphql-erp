package tr.kontas.erp.app.shipment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import tr.kontas.erp.app.salesorder.dtos.ShippingAddressPayload;

import java.util.List;

@Data
@AllArgsConstructor
public class ShipmentPayload {
    private String id;
    private String companyId;
    private String number;
    private String deliveryOrderId;
    private String salesOrderId;
    private String warehouseId;
    private ShippingAddressPayload shippingAddress;
    private String trackingNumber;
    private String carrierName;
    private String status;
    private String dispatchedAt;
    private String deliveredAt;
    private List<ShipmentLinePayload> lines;
}

