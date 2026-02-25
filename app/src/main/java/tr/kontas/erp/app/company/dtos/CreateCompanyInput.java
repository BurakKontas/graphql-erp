package tr.kontas.erp.app.company.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.company.validators.CreateCompanyInputValidator;

@Data
@Validate(validator = CreateCompanyInputValidator.class)
public class CreateCompanyInput implements Validatable {
    private String name;
}
