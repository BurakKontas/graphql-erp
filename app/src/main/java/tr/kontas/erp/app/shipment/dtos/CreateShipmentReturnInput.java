package tr.kontas.erp.app.shipment.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateShipmentReturnInput {
    private String companyId;
    private String shipmentId;
    private String salesOrderId;
    private String warehouseId;
    private String reason;
    private List<LineInput> lines;

    @Data
    public static class LineInput {
        private String shipmentLineId;
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal quantity;
        private String lineReason;
    }
}

