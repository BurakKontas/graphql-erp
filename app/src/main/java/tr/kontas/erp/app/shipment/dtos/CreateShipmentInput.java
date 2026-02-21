package tr.kontas.erp.app.shipment.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateShipmentInput {
    private String companyId;
    private String deliveryOrderId;
    private String salesOrderId;
    private String warehouseId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateOrProvince;
    private String postalCode;
    private String countryCode;
    private List<LineInput> lines;

    @Data
    public static class LineInput {
        private String deliveryOrderLineId;
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal quantity;
    }
}

