package tr.kontas.erp.app.shipment.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.shipment.validators.SetTrackingInfoInputValidator;

@Data
@Validate(validator = SetTrackingInfoInputValidator.class)
public class SetTrackingInfoInput implements Validatable {
    private String shipmentId;
    private String carrierName;
    private String trackingNumber;
}
