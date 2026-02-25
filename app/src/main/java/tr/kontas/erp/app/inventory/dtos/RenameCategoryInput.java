package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.inventory.validators.RenameCategoryInputValidator;

@Data
@Validate(validator = RenameCategoryInputValidator.class)
public class RenameCategoryInput implements Validatable {
    private String categoryId;
    private String name;
}
