package tr.kontas.erp.app.reference.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.reference.validators.CreateUnitInputValidator;

@Data
@Validate(validator = CreateUnitInputValidator.class)
public class CreateUnitInput implements Validatable {
    private String code;
    private String name;
    private String type;
}
