package tr.kontas.erp.app.employee.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.employee.validators.CreateEmployeeInputValidator;

@Data
@Validate(validator = CreateEmployeeInputValidator.class)
public class CreateEmployeeInput implements Validatable {
    private String name;
    private String departmentId;
}


