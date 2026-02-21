package tr.kontas.erp.app.shipment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ShipmentReturnPayload {
    private String id;
    private String companyId;
    private String number;
    private String shipmentId;
    private String salesOrderId;
    private String warehouseId;
    private String reason;
    private String status;
    private String receivedAt;
    private List<ShipmentReturnLinePayload> lines;
}

