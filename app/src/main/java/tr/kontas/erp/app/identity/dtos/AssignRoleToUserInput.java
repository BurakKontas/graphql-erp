package tr.kontas.erp.app.identity.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.identity.validators.AssignRoleToUserInputValidator;

@Data
@Validate(validator = AssignRoleToUserInputValidator.class)
public class AssignRoleToUserInput implements Validatable {
    private String userId;
    private String roleId;
}
