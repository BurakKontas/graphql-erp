package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.inventory.validators.ReserveStockInputValidator;

import java.math.BigDecimal;

@Data
@Validate(validator = ReserveStockInputValidator.class)
public class ReserveStockInput implements Validatable {
    private String itemId;
    private String warehouseId;
    private BigDecimal quantity;
    private String referenceId;
}
