package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreateLeavePolicyInputValidator;

@Data
@Validate(validator = CreateLeavePolicyInputValidator.class)
public class CreateLeavePolicyInput implements Validatable {
    private String companyId;
    private String name;
    private String countryCode;
}
