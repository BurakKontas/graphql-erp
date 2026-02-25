package tr.kontas.erp.app.identity.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.identity.validators.RemoveRoleFromUserInputValidator;

@Data
@Validate(validator = RemoveRoleFromUserInputValidator.class)
public class RemoveRoleFromUserInput implements Validatable {
    private String userId;
    private String roleId;
}
