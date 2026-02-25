package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.inventory.validators.AdjustStockInputValidator;

import java.math.BigDecimal;

@Data
@Validate(validator = AdjustStockInputValidator.class)
public class AdjustStockInput implements Validatable {
    private String itemId;
    private String warehouseId;
    private BigDecimal adjustment;
}
