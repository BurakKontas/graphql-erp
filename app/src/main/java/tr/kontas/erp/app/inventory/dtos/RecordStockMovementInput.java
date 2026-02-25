package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.inventory.validators.RecordStockMovementInputValidator;

import java.math.BigDecimal;

@Data
@Validate(validator = RecordStockMovementInputValidator.class)
public class RecordStockMovementInput implements Validatable {
    private String companyId;
    private String itemId;
    private String warehouseId;
    private String movementType;
    private BigDecimal quantity;
    private String referenceType;
    private String referenceId;
    private String note;
    private String movementDate;
}
