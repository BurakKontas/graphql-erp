package tr.kontas.erp.app.tenant.dtos;

import lombok.Data;
import tr.kontas.erp.app.tenant.validators.CreateTenantInputValidator;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;

@Data
@Validate(validator = CreateTenantInputValidator.class)
public class CreateTenantInput implements Validatable {
    private String name;
    private String code;
}
