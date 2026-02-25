package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.inventory.validators.UpdateItemInputValidator;

@Data
@Validate(validator = UpdateItemInputValidator.class)
public class UpdateItemInput implements Validatable {
    private String itemId;
    private String name;
    private String unitCode;
    private String categoryId;
}
