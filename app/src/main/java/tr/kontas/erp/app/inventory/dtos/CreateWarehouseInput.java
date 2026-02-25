package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.inventory.validators.CreateWarehouseInputValidator;

@Data
@Validate(validator = CreateWarehouseInputValidator.class)
public class CreateWarehouseInput implements Validatable {
    private String companyId;
    private String code;
    private String name;
}
