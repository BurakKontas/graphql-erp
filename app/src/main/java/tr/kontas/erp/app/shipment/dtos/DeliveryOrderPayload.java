package tr.kontas.erp.app.shipment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import tr.kontas.erp.app.salesorder.dtos.ShippingAddressPayload;

import java.util.List;

@Data
@AllArgsConstructor
public class DeliveryOrderPayload {
    private String id;
    private String companyId;
    private String number;
    private String salesOrderId;
    private String salesOrderNumber;
    private String customerId;
    private ShippingAddressPayload shippingAddress;
    private String status;
    private List<DeliveryOrderLinePayload> lines;
}


