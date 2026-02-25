package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.inventory.validators.RenameWarehouseInputValidator;

@Data
@Validate(validator = RenameWarehouseInputValidator.class)
public class RenameWarehouseInput implements Validatable {
    private String warehouseId;
    private String name;
}
