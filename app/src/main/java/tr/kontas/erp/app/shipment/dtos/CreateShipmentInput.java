package tr.kontas.erp.app.shipment.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.shipment.validators.CreateShipmentInputValidator;

import java.math.BigDecimal;
import java.util.List;

@Data
@Validate(validator = CreateShipmentInputValidator.class)
public class CreateShipmentInput implements Validatable {
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
