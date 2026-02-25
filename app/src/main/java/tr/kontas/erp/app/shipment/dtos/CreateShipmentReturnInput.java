package tr.kontas.erp.app.shipment.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.shipment.validators.CreateShipmentReturnInputValidator;

import java.math.BigDecimal;
import java.util.List;

@Data
@Validate(validator = CreateShipmentReturnInputValidator.class)
public class CreateShipmentReturnInput implements Validatable {
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
