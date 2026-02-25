package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.inventory.validators.SetReorderPointInputValidator;

import java.math.BigDecimal;

@Data
@Validate(validator = SetReorderPointInputValidator.class)
public class SetReorderPointInput implements Validatable {
    private String stockLevelId;
    private BigDecimal reorderPoint;
}
