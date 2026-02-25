package tr.kontas.erp.app.inventory.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.inventory.validators.CreateItemInputValidator;

@Data
@Validate(validator = CreateItemInputValidator.class)
public class CreateItemInput implements Validatable {
    private String companyId;
    private String code;
    private String name;
    private String type;
    private String unitCode;
    private String categoryId;
    private Boolean allowNegativeStock;
}
