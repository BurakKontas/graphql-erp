package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.inventory.validators.ReleaseReservationInputValidator;

import java.math.BigDecimal;

@Data
@Validate(validator = ReleaseReservationInputValidator.class)
public class ReleaseReservationInput implements Validatable {
    private String itemId;
    private String warehouseId;
    private BigDecimal quantity;
    private String referenceId;
}
