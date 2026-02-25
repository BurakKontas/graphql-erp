package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.inventory.validators.CreateCategoryInputValidator;

@Data
@Validate(validator = CreateCategoryInputValidator.class)
public class CreateCategoryInput implements Validatable {
    private String companyId;
    private String name;
    private String parentCategoryId;
}
