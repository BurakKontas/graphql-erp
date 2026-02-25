package tr.kontas.erp.app.department.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.department.validators.CreateDepartmentInputValidator;

@Data
@Validate(validator = CreateDepartmentInputValidator.class)
public class CreateDepartmentInput implements Validatable {
    private String name;
    private String companyId;
    private String parentId; // nullable
}
